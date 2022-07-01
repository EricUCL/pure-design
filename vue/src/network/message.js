import {request} from "./request"
export function getMessage(data) {
    return request({
        url: 'basicMessage',
        // 默认是 get
        method: 'post',
        data: data
    })
}