package com.example.downserver.config;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * SseServer业务封装类来操作SEE
 */
@Slf4j
public class SseServer {
    /**
     * 当前连接总数
     */
    private static AtomicInteger currentConnectTotal = new AtomicInteger(0);
    /**
     * messageId的 SseEmitter对象映射集
     */
    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
    /**
     * 创建sse连接
     *
     * @param messageId - 消息id（唯一）
     * @return
     */
    public static SseEmitter createConnect(String messageId) {
        /**
         * 设置连接超时时间。0表示不过期，默认是30秒，超过时间未完成会抛出异常
         */
        SseEmitter sseEmitter = new SseEmitter(0L);
        /*
        // 超时时间设置为3s，设置前端的重试时间为1s。重连时，注意总数的统计
        SseEmitter sseEmitter = new SseEmitter(3_000L);
        try {
            sseEmitter.send(
                    SseEmitter.event()
                    .reconnectTime(1000L)
                    //.data("前端重连成功") // 重连成功的提示信息
            );
        } catch (IOException e) {
            log.error("前端重连异常 ==> messageId={}, 异常信息：", messageId, e.getMessage());
            e.printStackTrace();
        }*/
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(messageId));
        sseEmitter.onTimeout(timeOutCallBack(messageId));
        sseEmitter.onError(errorCallBack(messageId));
        sseEmitterMap.put(messageId, sseEmitter);
        //记录一下连接总数。数量+1
        int count = currentConnectTotal.incrementAndGet();
        log.info("创建sse连接成功 ==> 当前连接总数={}， messageId={}", count, messageId);
        return sseEmitter;
    }
    /**
     * 给指定 messageId发消息
     *
     * @param messageId - 消息id（唯一）
     * @param message   - 消息文本
     */
    public static void sendMessage(String messageId, String message) {
        if (sseEmitterMap.containsKey(messageId)) {
            try {
                sseEmitterMap.get(messageId).send(message);
            } catch (IOException e) {
                log.error("发送消息异常 ==> messageId={}, 异常信息：", messageId, e.getMessage());
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("连接不存在或者超时， messageId=" + messageId);
        }
    }
    /**
     * 给所有 messageId广播发送消息
     *
     * @param message
     */
    public static void batchAllSendMessage(String message) {
        sseEmitterMap.forEach((messageId, sseEmitter) -> {
            try {
                sseEmitter.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                log.error("广播发送消息异常 ==> messageId={}, 异常信息：", messageId, e.getMessage());
                removeMessageId(messageId);
            }
        });
    }
    /**
     * 给指定 messageId集合群发消息
     *
     * @param messageIds
     * @param message
     */
    public static void batchSendMessage(List<String> messageIds, String message) {
        if (CollectionUtils.isEmpty(messageIds)) {
            return;
        }
        // 去重
        messageIds = messageIds.stream().distinct().collect(Collectors.toList());
        messageIds.forEach(userId -> sendMessage(userId, message));
    }
    /**
     * 给指定组群发消息（即组播，我们让 messageId满足我们的组命名确定即可）
     *
     * @param groupId
     * @param message
     */
    public static void groupSendMessage(String groupId, String message) {
        if (MapUtils.isEmpty(sseEmitterMap)) {
            return;
        }
        sseEmitterMap.forEach((messageId, sseEmitter) -> {
            try {
                // 这里 groupId作为前缀
                if (messageId.startsWith(groupId)) {
                    sseEmitter.send(message, MediaType.APPLICATION_JSON);
                }
            } catch (IOException e) {
                log.error("组播发送消息异常 ==> groupId={}, 异常信息：", groupId, e.getMessage());
                removeMessageId(messageId);
            }
        });
    }
    /**
     * 移除 MessageId
     *
     * @param messageId
     */
    public static void removeMessageId(String messageId) {
        sseEmitterMap.remove(messageId);
        //数量-1
        currentConnectTotal.getAndDecrement();
        log.info("remove messageId={}", messageId);
    }
    /**
     * 获取所有的 MessageId集合
     *
     * @return
     */
    public static List<String> getMessageIds() {
        return new ArrayList<>(sseEmitterMap.keySet());
    }
    /**
     * 获取当前连接总数
     *
     * @return
     */
    public static int getConnectTotal() {
        return currentConnectTotal.intValue();
    }
    /**
     * 断开SSE连接时的回调
     *
     * @param messageId
     * @return
     */
    private static Runnable completionCallBack(String messageId) {
        return () -> {
            log.info("结束连接 ==> messageId={}", messageId);
            removeMessageId(messageId);
        };
    }
    /**
     * 连接超时时回调触发
     *
     * @param messageId
     * @return
     */
    private static Runnable timeOutCallBack(String messageId) {
        return () -> {
            log.info("连接超时 ==> messageId={}", messageId);
            removeMessageId(messageId);
        };
    }
    /**
     * 连接报错时回调触发。
     *
     * @param messageId
     * @return
     */
    private static Consumer<Throwable> errorCallBack(String messageId) {
        return throwable -> {
            log.error("连接异常 ==> messageId={}", messageId);
            removeMessageId(messageId);
        };
    }
}