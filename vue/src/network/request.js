import axios from "axios";
import NProgress from "nprogress"
import 'nprogress/nprogress.css'

export function request(config) {
    const instance = axios.create({
        baseURL: 'http://localhost:8899/',
        timeout: 5000
    })
    instance.interceptors.request.use(config => {
        NProgress.start()
        return config
    }, error => {
        console.log(error)
    })
    instance.interceptors.response.use(config => {
        NProgress.done()
        return config
    }, error => {
        console.log(error)
        return error.response
    })
    return instance(config)
}