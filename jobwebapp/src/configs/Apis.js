import axios from "axios"
import cookie from 'react-cookies'

const BASE_URL = 'http://localhost:8080/JobSearchWebApp/api/'


export const endpoints = {
    'categories': '/categories',
    'jobpostings': '/jobs',
    'createjob': '/createjob',
    'register': '/users',
    'login': 'logins',
    'current-user': '/secure/profile', 'get_all_applications': '/JobApplications',
    'jaOfUser': '/jobapplications',
    'addapplication':'/addapplication',
    'accept': '/jobapplications/accept/',
    'reject': '/jobapplication/reject/',
    'feedbacks': '/feedback',
    'addFeedback': '/feedback/add/',
    'companies': '/companies',
    'companyOfUser': '/company/by-user/',
    'follows': '/follow/',
    'followed': '/followed',//danh sach công ty đã theo dõi
    'users_work_with': '/worked-with',
    'fbOfUser': '/feedback/user/',
    'fbOfUser_1': '/feedback/user/1',
    'inactiveUsers': '/users/inactive',
    'approveUser': '/users/approve/',
    'stats': '/stats/revenue'


}

export const authApis = () => {//object để tạo request có token
    const token = cookie.load('token');
    console.log("authApis - Token from cookie:", token); // Debug
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${cookie.load('token')}`,
        }
    })
}


export default axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});