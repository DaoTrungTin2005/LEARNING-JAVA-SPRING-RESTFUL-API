package com.example.Jobhunter.domain.dto;


// Nó dùng để đóng gói dữ liệu trả về cho client sau khi đăng nhập thành công.
//In ra cái accessToken kiểu :
// {
//     "statusCode": 200,
//     "error": null,
//     "message": "Call API success",
//     "data": {
//         "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2lkYW5pdEBnbWFpbC5jb20iLCJob2lkYW5pdCI6eyJwcmluY2lwYWwiOnsicGFzc3dvcmQiOm51bGwsInVzZXJuYW1lIjoiaG9pZGFuaXRAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlt7InJvbGUiOiJST0xFX1VTRVIifV0sImFjY291bnROb25FeHBpcmVkIjp0cnVlLCJhY2NvdW50Tm9uTG9ja2VkIjp0cnVlLCJjcmVkZW50aWFsc05vbkV4cGlyZWQiOnRydWUsImVuYWJsZWQiOnRydWV9LCJjcmVkZW50aWFscyI6bnVsbCwiYXV0aG9yaXRpZXMiOlt7InJvbGUiOiJST0xFX1VTRVIifV0sImRldGFpbHMiOm51bGwsImF1dGhlbnRpY2F0ZWQiOnRydWV9LCJleHAiOjE3NTg1OTY4NTMsImlhdCI6MTc1ODUxMDQ1M30.zCFryNtIg8OmFz1HPI1iy0ovnKHHewrl6e2vtejYnqNMk1GRS9NCqrJi44DTE2646n35l49tyI0L8sNBuO-LHQ"
//     }
// }

public class RestLoginDTO {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
