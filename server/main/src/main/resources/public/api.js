/**
 * @returns {Array} 返回所有的设置项
 */
async function axiosRequest(method, urlSuffix, data) {
    const baseUrl = "http://localhost:8011";
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

// 设置页面 api,下面是需要返回给我的数据格式
async function fetchSettings() {
    try {
        const response = await fetch('/settings');
        if (!response.ok) {
            throw new Error('request failed');
        }

        const data = await response.json();

        console.log('asd',data)
        return {
            downloadPath: data.setting.storePath,
            maxTasks: data.setting.maxTasks,
            maxDownloadSpeed: data.setting.maxDownloadSpeed,
            maxUploadSpeed: data.setting.maxUploadSpeed
        };
    } catch (error) {
        console.error(error);
        // 返回默认值或进行其他错误处理逻辑
        return {
            downloadPath: '',
            maxTasks: 0,
            maxDownloadSpeed: 0,
            maxUploadSpeed: 0
        };
    }
}

// 保存设置
async function saveSettings(settings) {
    // console.log(settings)
    // return true
    try {
        const data = {
            storePath: settings.downloadPath,
            maxTasks: settings.maxTasks,
            maxDownloadSpeed: settings.maxDownloadSpeed,
            maxUploadSpeed: settings.maxUploadSpeed
        };

        const response = await fetch('/settings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error('request failed');
            alert('failed to save')
            return false
        }
        alert('save successfully')
        return true
    } catch (error) {
        console.error(error);
    }
}

// file 页面 api

// TODO: 获取文件列表
// 如果 没有path参数或者参数是/file，那么就是获取根目录下的文件列表
// 如果有path参数，那么就是获取path目录下的文件列表

async function fetchFileList(path, filter) {
    console.log(path, filter)
    if (filter === undefined) {
        if (path === undefined || path === '/file') {
            return [
                {
                    name: 'Test1',
                    isDirectory: true,
                    path: '/data/mock_dir/test1',
                    size: 0,
                    createdAt: '2023-11-11',
                    children: [
                        {
                            name: 'Test1-1',
                            isDirectory: true,
                            path: '/test1-1',
                            size: null,
                            createdAt: '2023-11-11',
                            children: [{
                                name: 'Test1-1-1',
                                isDirectory: true,
                                path: '/test1-1-1',
                                size: '100KB',
                                createdAt: '2023-11-11',
                                children: [{
                                    name: 'Test1-1-1-1',
                                    isDirectory: false,
                                    type: 'txt',
                                    path: '/test1-1-1-1',
                                    size: '100KB',
                                    createdAt: '2023-11-11'
                                }]
                            }]
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
        }
        if (path === '/data/mock_dir/test1') {
            return [
                {
                    name: 'Test1-1',
                    isDirectory: true,
                    path: '/test1-1',
                    size: null,
                    createdAt: '2023-11-11',
                    children: [{
                        name: 'Test1-1-1',
                        isDirectory: true,
                        path: '/test1-1-1',
                        size: '100KB',
                        createdAt: '2023-11-11',
                        children: [{
                            name: 'Test1-1-1-1',
                            isDirectory: false,
                            type: 'txt',
                            path: '/test1-1-1-1',
                            size: '100KB',
                            createdAt: '2023-11-11'
                        }]
                    }]
                },
                {
                    name: 'Test1-2.pdf',
                    isDirectory: false,
                    type: 'pdf',
                    path: '/test1-2',
                    size: '100KB',
                    createdAt: '2023-11-11'
                }]
        }
        if (path === '/data/mock_dir/test2') {
            return [
                {
                    name: 'Test2.txt',
                    isDirectory: false,
                    type: 'txt',
                    path: '/data/mock_dir/test2',
                    size: '20KB',
                    createdAt: '2023-11-13'
                }
            ]
        }
        if (path === '/test1-1') {
            return [
                {
                    name: 'Test1-1-1',
                    isDirectory: true,
                    path: '/test1-1-1',
                    size: '100KB',
                    createdAt: '2023-11-11',
                    children: [{
                        name: 'Test1-1-1-1',
                        isDirectory: false,
                        type: 'txt',
                        path: '/test1-1-1-1',
                        size: '100KB',
                        createdAt: '2023-11-11'
                    }]
                }
            ]
        }
        if (path === '/test1-1-1') {
            return [
                {
                    name: 'Test1-1-1-1',
                    isDirectory: false,
                    type: 'txt',
                    path: '/test1-1-1-1',
                    size: '100KB',

                    createdAt: '2023-11-11'
                }
            ]
        }
    } else {
        return {
            name: 'test.测试用例类型',
            isDirectory: false,
            type: '测试用例类型',
            path: '/data/mock_dir/test3',
            size: '1MB',
            createdAt: '2023-11-12'
        }
    }
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
        status: "",
        url: ""
    };

    try {
        const responseData = await axiosRequest("POST", "task/list/page/vo", requestData);

        // 在这里对返回的数据进行适配，添加 total 属性等
        const adaptedData = {
            total: responseData.data.size, // 使用后端返回的总数信息
            records: responseData.data.records
        };

        // 返回适配后的数据
        return adaptedData;
    } catch (error) {
        console.error('Error in fetchTasks:', error);
        throw error; // 抛出错误，以便调用方知道请求失败
    }
    // return axiosRequest("POST", "task/list/page/vo",requestData).then(data => {
    //     console.log(data.records);
    //     return data
    // })
    //     .catch(error => {
    //         // 错误处理已经在 axiosRequest 中完成，这里无需重复处理
    //     });
    // return {
    //     total: 6,
    //     records: [
    //         {
    //             id: '111',
    //             type: 'http',
    //             url: 'https://mirrors.tuna.tsinghua.edu.cn/aaa/ubuntu-live-server-arm.iso',
    //             name: 'ubuntu.iso',
    //             status: 'do',
    //             size: '4GB',
    //             speed: '111MB',
    //             progress: '40',
    //             createdAt: '2023-11-11',
    //             finishedAt: '2023-11-12',
    //             timeLeft: '3m 10s',
    //             peers: 12,
    //             downloadSpeed: '140KB',
    //         },
    //         {
    //             id: '111',
    //             type: 'http',
    //             url: 'https://mirrors.tuna.tsinghua.edu.cn/aaa/ubuntu-live-server-arm.iso',
    //             name: 'ubuntu.iso',
    //             status: 'do',
    //             size: '4GB',
    //             speed: '111MB',
    //             progress: '40',
    //             createdAt: '2023-11-11',
    //             finishedAt: '2023-11-12',
    //             timeLeft: '3m 10s',
    //             peers: 12,
    //             downloadSpeed: '140KB',
    //         },
    //         {
    //             id: '111',
    //             type: 'http',
    //             url: 'https://mirrors.tuna.tsinghua.edu.cn/aaa/ubuntu-live-server-arm.iso',
    //             name: 'ubuntu.iso',
    //             status: 'do',
    //             size: '4GB',
    //             speed: '111MB',
    //             progress: '40',
    //             createdAt: '2023-11-11',
    //             finishedAt: '2023-11-12',
    //             timeLeft: '3m 10s',
    //             peers: 12,
    //             downloadSpeed: '140KB',
    //         },{
    //             id: '111',
    //             type: 'http',
    //             url: 'https://mirrors.tuna.tsinghua.edu.cn/aaa/ubuntu-live-server-arm.iso',
    //             name: 'ubuntu.iso',
    //             status: 'do',
    //             size: '4GB',
    //             speed: '111MB',
    //             progress: '40',
    //             createdAt: '2023-11-11',
    //             finishedAt: '2023-11-12',
    //             timeLeft: '3m 10s',
    //             peers: 12,
    //             downloadSpeed: '140KB',
    //         },{
    //             id: '111',
    //             type: 'http',
    //             url: 'https://mirrors.tuna.tsinghua.edu.cn/aaa/ubuntu-live-server-arm.iso',
    //             name: 'ubuntu.iso',
    //             status: 'do',
    //             size: '4GB',
    //             speed: '111MB',
    //             progress: '40',
    //             createdAt: '2023-11-11',
    //             finishedAt: '2023-11-12',
    //             timeLeft: '3m 10s',
    //             peers: 12,
    //             downloadSpeed: '140KB',
    //         },{
    //             id: '111',
    //             type: 'http',
    //             url: 'https://mirrors.tuna.tsinghua.edu.cn/aaa/ubuntu-live-server-arm.iso',
    //             name: 'ubuntu.iso',
    //             status: 'do',
    //             size: '4GB',
    //             speed: '111MB',
    //             progress: '40',
    //             createdAt: '2023-11-11',
    //             finishedAt: '2023-11-12',
    //             timeLeft: '3m 10s',
    //             peers: 12,
    //             downloadSpeed: '140KB',
    //         },{
    //             id: '111',
    //             type: 'http',
    //             url: 'https://mirrors.tuna.tsinghua.edu.cn/aaa/ubuntu-live-server-arm.iso',
    //             name: 'ubuntu.iso',
    //             status: 'do',
    //             size: '4GB',
    //             speed: '111MB',
    //             progress: '40',
    //             createdAt: '2023-11-11',
    //             finishedAt: '2023-11-12',
    //             timeLeft: '3m 10s',
    //             peers: 12,
    //             downloadSpeed: '140KB',
    //         },
    //     ]
    // }
}


async function submitDownloadPath(path) {
    console.log(path)
    const requestData = {
        url: path
    };
    return axiosRequest("POST", "task/submit",requestData).then(data => {
        this.message = data.message; // 假设响应包含 'message' 属性
        console.log(data.message);
    })
        .catch(error => {
            // 错误处理已经在 axiosRequest 中完成，这里无需重复处理
        });
}

// 重新下载任务的详细信息，ids是一个数组，单个任务，就是一个元素的数组，多个任务就是多个元素的数组，实现同一个接口单量和多量的处理
async function fetchTaskInfo(ids) {
    console.log(ids)
    // return {
    //     id: 'refreshed_id',
    //     type: 'http',
    //     url: 'https://mirrors.tuna.tsinghua.edu.cn/ubuntu-releases/22.04.3/ubuntu-22.04.3-live-server-arm.iso',
    //     name: 'ubuntu-22.04.3-live-server-arm.iso',
    //     status: 'downloading',
    //     size: '3GB',
    //     speed: '2.3MB',
    //     progress: '80',
    //     createdAt: '2023-11-11',
    //     finishedAt: '2023-11-12',
    //     timeLeft: '3m 10s',
    //     peers: 12,
    //     downloadSpeed: '140KB',
    // }
}

// 暂停下载任务,ids是一个数组，单个任务，就是一个元素的数组，多个任务就是多个元素的数组，实现同一个接口单量和多量的处理
async function pauseTask(ids) {
    console.log(ids)
    return true
}



// 恢复下载任务，ids是一个数组
async function resumeTask(ids) {
    console.log(ids)
    return true
}

// 删除下载任务，ids是一个数组
async function deleteTask(ids) {
    console.log(ids)
    return true
}

// 对任务的状态进行过滤选择,如果是all 的情况下，就返回所有的数据，默认是all 的情况
async function fetchFilterTasks(filter) {
    if (filter === 'all') {
        return {
            total: 6,
            items: [
                {
                    id: '111',
                    type: 'http',
                    url: 'https://mirrors.tuna.tsinghua.edu.cn/ubuntu-releases/22.04.3/ubuntu-22.04.3-live-server-arm.iso',
                    name: 'ubuntu.iso',
                    status: 'downloading',
                    size: '3GB',
                    speed: '2.3MB',
                    progress: '80',
                    createdAt: '2023-11-11',
                    finishedAt: '2023-11-12',
                    timeLeft: '3m 10s',
                    peers: 12,
                    downloadSpeed: '140KB',
                },
                {
                    id: '222',
                    type: 'http',
                    url: 'https://mirrors.tuna.tsinghua.edu.cn/ubuntu-releases/22.04.3/ubuntu-22.04.3-live-server-arm.iso',
                    name: 'ubuntu-22.04.3-live-server-arm.iso',
                    status: 'pending',
                    size: '9MB',
                    speed: '1.2MB',
                    progress: '0',
                    createdAt: '2023/11/10 00:00:00',
                    finishedAt: '2023-11-15',
                    peers: 12,
                },
                {
                    id: '333',
                    type: 'bt',
                    name: 'ubuntu-22.04.3-live-server-arm.iso',
                    status: 'downloading',
                    size: '2.6GB',
                    speed: '120KB',
                    progress: '10',
                    createdAt: '2023-11-11',
                    finishedAt: '2023-11-12',
                    timeLeft: '3m 2s',
                    peers: 10,
                    uploadSpeed: '198KB',
                    downloadSpeed: '198KB',
                },
                {
                    id: '444',
                    type: 'http',
                    url: 'https://mirrors.tuna.tsinghua.edu.cn/ubuntu-releases/22.04.3/ubuntu-22.04.3-live-server-arm.iso',
                    name: 'ubuntu-22.04.3-live-server-arm.iso',
                    status: 'downloaded',
                    size: '9MB',
                    speed: '1.2MB',
                    progress: '100',
                    createdAt: '2023/11/10 00:00:00',
                    finishedAt: '2023-11-15',
                    timeLeft: '1m 20s',
                    peers: 12,
                    downloadSpeed: '1.2MB',
                },
                {
                    id: '555',
                    type: 'bt',
                    name: 'ubuntu-22.04.3-live-server-arm.iso',
                    status: 'downloading',
                    size: '2.6GB',
                    speed: '120KB',
                    progress: '10',
                    createdAt: '2023-11-11',
                    finishedAt: '2023-11-12',
                    timeLeft: '3m 2s',
                    peers: 10,
                    uploadSpeed: '198KB',
                    downloadSpeed: '198KB',
                },
                {
                    id: '666',
                    type: 'http',
                    url: 'https://mirrors.tuna.tsinghua.edu.cn/ubuntu-releases/22.04.3/ubuntu-22.04.3-live-server-arm.iso',
                    name: 'ubuntu-22.04.3-live-server-arm.iso',
                    status: 'downloaded',
                    size: '9MB',
                    speed: '1.2MB',
                    progress: '100',
                    createdAt: '2023/11/10 00:00:00',
                    finishedAt: '2023-11-15',
                    timeLeft: '1m 20s',
                    peers: 12,
                    downloadSpeed: '1.2MB',
                }
            ]
        }
    } else {
        return {
            total: 1,
            items: [
                {
                    id: '111',
                    type: 'http',
                    url: 'https://mirrors.tuna.tsinghua.edu.cn/ubuntu-releases/22.04.3/ubuntu-22.04.3-live-server-arm.iso',
                    name: 'mock过滤后的数据',
                    status: 'downloading',
                    size: '3GB',
                    speed: '2.3MB',
                    progress: '80',
                    createdAt: '2023-11-11',
                    finishedAt: '2023-11-12',
                    timeLeft: '3m 10s',
                    peers: 12,
                    downloadSpeed: '140KB',
                }
            ]
        }
    }
}