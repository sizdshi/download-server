// const BASE_URL = "http://localhost:8080"
const BASE_URL = "http://tctech.asia:8011"


// setting page api
async function fetchSettings() {
    try {
        const resp = await fetch(`${BASE_URL}/settings`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
        const data = await resp.json()
        console.log(data, 'data')
        return data.setting
    } catch (e) {
        console.log(e)
    }
}

async function saveSettings(settings) {
    console.log(settings)
    try {
        await fetch(`${BASE_URL}/settings`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(settings)
        })
        alert('save successfully')
        return true
    } catch (error) {
        console.log(error)
    }
}

//file page api
async function fetchFiles(path) {
    try {
        const resp = await fetch(`${BASE_URL}/files?path=${path}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
        const data = await resp.json()
        return data
    } catch (e) {
        console.log(e)
    }
}


// TODO: 获取文件列表，过滤可以一起做了，四个参数 path, type, sort，order（正序/倒序）
// 进行排序的时候，参数放在body里面，不要放在url里面

async function fetchFileList(params) {
    console.log(params)
        const resp = await fetch(BASE_URL + "/file/list", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(params)
        })
            const data = await resp.json()
            console.log(data, 'data json')
            return data
}
