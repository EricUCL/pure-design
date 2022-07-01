import {request} from "./request"

export function getStaticDoc() {
  return request({
    url: `/static/food`
  })
}