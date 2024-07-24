import * as URL from "@/constant/host.js";
const http = ({ ur1, method = "GET", data = "", header = null }) => {
  // 服务器环境
  // const URL = 'http://124.71.96.241:8080'
  // 本地环境（内网穿透）
  //const URL = 'http://124.71.96.241:8081'
  return new Promise((resolve, reject) => {
    uni.request({
      method: method,
      url: URL.HOST + ur1,
      data: data,
      header: header,
      success: (res) => {
        console.log(res.data);
        resolve(res.data);
      },
      fail: (res) => {
        console.log(res);
        throw res;
      },
    });
  });
};
export default http;
