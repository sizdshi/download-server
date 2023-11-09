package com.example.downloadserver.mapper;

import com.example.downloadserver.dataobject.SettingDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SettingMapper {

    @Select("SELECT store_path as storePath,max_tasks as maxTasks,max_download_speed as maxDownloadSpeed,max_upload_speed as maxUploadSpeed FROM setting WHERE id=1")
    SettingDO get();

    @Update("update setting set store_path=#{storePath},max_tasks=#{maxTasks},max_download_speed=#{maxDownloadSpeed},max_upload_speed=#{maxUploadSpeed} where id=1")
    int update(SettingDO settingDO);
}
