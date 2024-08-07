/**
 * @returns {Array} 返回所有的设置项
 */
async function sseRequest(){
    const userId = new Date().toString();
    var sseSource = new EventSource("http://localhost:8012/sse/userConnect?userId="+userId);

    sseSource.onopen=(e)=>{
        console.log("test=>",e);
        console.log("sse连接成功");
    }
    <!-- 添加一个信息回调 -->
    // return sseSource.onmessage = function(event){
    //     console.log("test=>",event)
    //     // document.getElementById("result").innerText = event.data+'%';
    //     // document.getElementById("my-progress").value = event.data;
    // }

}

async function axiosRequest(method, urlSuffix, data) {
    console.log(1111)
    const baseUrl = "http://localhost:8011";
    //const baseUrl = 'http://tctech.asia:8011';
    const fullUrl = `${baseUrl}/${urlSuffix}`;

    return axios({
        method: method,
        url: fullUrl,
        data: data,
        params: method === "GET" ? data : undefined,
    })
        .then(response => response.data)
        .catch(error => {
            console.error('错误:', error);
            throw error;
        });
}

async function axiosDownloadRequest(method, urlSuffix, data) {
    console.log("请求Download")
    const baseUrl = "http://localhost:8012";
    const fullUrl = `${baseUrl}/${urlSuffix}`;

    return axios({
        method: method,
        url: fullUrl,
        data: data,
        params: method === "GET" ? data : undefined,
    })
        .then(response => response.data)
        .catch(error => {
            console.error('错误:', error);
            throw error;
        });
}

async function download(params){
    const requestData={
        "id": params.id,
        "savePath": params.savePath,
        "threadCount": params.threadCount,
        "url": params.url
    }
    const data  = await axiosDownloadRequest('POST',"download/start",requestData);
    console.log(data)
    return data;
}

// 设置页面 api,下面是需要返回给我的数据格式
async function fetchSettings() {

    try {

        const data = await axiosRequest('GET', 'settings');

        console.log('asd', data)
        return {
            store_path: data.setting.store_path,
            max_tasks: data.setting.max_tasks,
            max_download_speed: data.setting.max_download_speed,
            max_upload_speed: data.setting.max_upload_speed
        };
    } catch (error) {
        console.error(error);
        // 返回默认值或进行其他错误处理逻辑
        return {
            store_path: '',
            max_tasks: 0,
            max_download_speed: 0,
            max_upload_speed: 0
        };
    }
}

// 保存设置
async function saveSettings(settings) {
    // console.log(settings)
    // return true
    try {
        const data = {
            store_path: settings.store_path,
            max_tasks: settings.max_tasks,
            max_download_speed: settings.max_download_speed,
            max_upload_speed: settings.max_upload_speed
        };

        await axiosRequest("POST", "settings", data)
        alert('save successfully')

        return true
    } catch (error) {
        console.error(error);
        return false
    }
}

// file 页面 api

// TODO: 获取文件列表
// 如果 没有path参数或者参数是/file，那么就是获取根目录下的文件列表
// 如果有path参数，那么就是获取path目录下的文件列表

async function fetchFileList(path, filter) {
    console.log(path, filter)

}

//  TODO: 查找符合path的文件，然后根据filter进行过滤
// 如果有filter参数，那么就是获取path目录下的文件列表，然后根据filter进行过滤,filter的值有：all,video,archive,document，我这里只需要返回一个符合filter的文件即可（后端详细分类）
async function fetchFilterFile(path, filter) {
    console.log(path, filter)
    if (filter === 'all') {
        return [
            {
                name: 'Test1',
                isDirectory: true,
                path: '/data/mock_dir/test1',
                size: 0,
                createdAt: '2023-11-11',
                children: [
                    {
                        name: 'Test1-1.txt',
                        isDirectory: false,
                        type: 'txt',
                        path: '/test1-1',
                        size: null,
                        createdAt: '2023-11-11'
                    },
                    {
                        name: 'Test1-2.pdf',
                        isDirectory: false,
                        type: 'pdf',
                        path: '/test1-2',
                        size: '100KB',
                        createdAt: '2023-11-11'
                    }]
            },
            {
                name: 'Test2.txt',
                isDirectory: false,
                type: 'txt',
                path: '/data/mock_dir/test2',
                size: '20KB',
                createdAt: '2023-11-13'
            },
            {
                name: 'Test3.gif',
                isDirectory: false,
                type: 'gif',
                path: '/data/mock_dir/test3',
                size: '1MB',
                createdAt: '2023-11-12'
            },
        ]
    } else
        return [
            {
                name: 'Test1',
                isDirectory: true,
                path: '/data/mock_dir/test1',
                size: 0,
                createdAt: '2023-11-11',
                children: [
                    {
                        name: 'Test1-1.txt',
                        isDirectory: false,
                        type: 'txt',
                        path: '/test1-1',
                        size: null,
                        createdAt: '2023-11-11'
                    }]
            },
            {
                name: 'test.测试用例类型',
                isDirectory: false,
                type: '测试用例类型',
                path: '/data/mock_dir/test3',
                size: '1MB',
                createdAt: '2023-11-12'
            }]
}

// tasks 页面 api
async function fetchTasks(params) {
    console.log(params)
    const requestData = {
        current: params.currentPage,
        file_name: "",
        id: "",
        pageSize: params.limit,
        sortField: "",
        sortOrder: "",
        status: params.status,
        url: ""
    };

    try {
        const responseData = await axiosRequest("POST", "task/list/page/vo", requestData);
        // 在这里对返回的数据进行适配，添加 total 属性等
        console.log(responseData);
        const adaptedData = {
            total: responseData.data.total, // 使用后端返回的总数信息
            records: responseData.data.records
        };
        console.log(adaptedData);
        // 返回适配后的数据
        return adaptedData;
    } catch (error) {
        console.error('Error in fetchTasks:', error);
        throw error; // 抛出错误，以便调用方知道请求失败
    }

}


async function submitDownloadPath(path) {
    console.log(path)
    const requestData = {
        url: path
    };

    const responseData = await axiosRequest("POST","task/submit",requestData);
    const { id, file_url: savePath, count: threadCount, url } = responseData.data;

    const newData = {
        id: id || "",
        savePath: savePath || "",
        threadCount: threadCount ,
        url: url || ""
    };

    console.log(newData);



    return newData;
}

// 重新下载任务的详细信息，ids是一个数组，单个任务，就是一个元素的数组，多个任务就是多个元素的数组，实现同一个接口单量和多量的处理
async function fetchTaskInfo(ids) {
    console.log("重新下载：" + ids)
    return axiosRequest("POST", "task/restart", ids).then(data => {
        console.log(data)
    })
}



// 暂停下载任务,ids是一个数组，单个任务，就是一个元素的数组，多个任务就是多个元素的数组，实现同一个接口单量和多量的处理
async function pauseTask(ids) {
    console.log("暂停下载：" + ids)
    return axiosRequest("POST", "task/suspend", ids).then(data => {
        console.log(data)
    })
}


// 恢复下载任务，ids是一个数组
async function resumeTask(ids) {
    console.log("开始下载:" + ids)
    return axiosRequest("POST", "task/start", ids).then(data => {
        console.log(data)
    })
}

// 删除下载任务，ids是一个数组
async function deleteTask(ids) {
    console.log("删除任务:" + ids)
    return axiosRequest("POST", "task/delete", ids).then(data => {
        console.log(data)
    })
}

// 对任务的状态进行过滤选择,如果是all 的情况下，就返回所有的数据，默认是all 的情况
async function fetchFilterTasks(params) {
    console.log(params)
    const requestData = {
        current: params.currentPage,
        file_name: "",
        id: "",
        pageSize: params.limit,
        sortField: "",
        sortOrder: "",
        status: params.status,
        url: ""
    };

    try {
        const responseData = await axiosRequest("POST", "task/list/page/vo", requestData);
        // 在这里对返回的数据进行适配，添加 total 属性等
        console.log(responseData);
        const adaptedData = {
            total: responseData.data.total, // 使用后端返回的总数信息
            records: responseData.data.records
        };
        console.log(adaptedData);
        // 返回适配后的数据
        return adaptedData;
    } catch (error) {
        console.error('Error in fetchTasks:', error);
        throw error; // 抛出错误，以便调用方知道请求失败
    }
}

async function changeThreads(id, count) {
    console.log("任务：" + id)
    console.log("任务数：" + count)
    const requestData={
        id:id,
        count:count
    }
    return axiosRequest("POST", "task/thread", requestData).then(data => {
        console.log(data)
    })
}

async function validateURL(url) {
    // 定义 URL 模式的正则表达式
    const urlPattern = /^(http|https):\/\/(www\.)?([a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*|\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})(:\d+)?(\/\S*)?$/;

    // 使用正则表达式进行匹配
    if (!urlPattern.test(url)) {
        // 弹窗显示错误信息
        alert("不合法的URL");
        return false;
    }

    return true;
}