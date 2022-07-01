import {request} from "./request"

export function generateFile() {
  return request({
    url: '/word/updateWord'
  })
}