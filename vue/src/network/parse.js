import {request} from "./request";
export function getFileData() {
    return request({
        url: 'parseFile'
    })
}
export function getParseData(column){
    return request({
        url: 'getParseData',
        method: 'post',
        data: {'checkedCities':column}
    })
}