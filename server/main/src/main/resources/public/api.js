/**
 * @returns {Array} 返回所有的设置项
 */

// 设置页面 api,下面是需要返回给我的数据格式
async function fetchSettings() {
    return {
        downloadPath: '/data/mock_dir',
        maxTasks: 4,
        maxDownloadSpeed: 10,
        maxUploadSpeed: 12
    }
}

// 保存设置
async function saveSettings(settings) {
    console.log(settings)
    return true
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
    return {
        total: 6,
        items: [
            {
                id: '111',
                type: 'http',
                url: 'https://mirrors.tuna.tsinghua.edu.cn/ubuntu-releases/22.04.3/ubuntu-22.04.3-live-server-arm.iso',
                name: 'ubuntu-22.04.3-live-server-arm.iso',
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
}

async function submitDownloadPath(path) {
    console.log(path)
    return true
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
                    name: 'ubuntu-22.04.3-live-server-arm.iso',
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