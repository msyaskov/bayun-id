import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import IndexRoute from "./index/IndexRoute";
import LoginRoute from "./index/login/LoginRoute";
import MainRoute from "./index/main/MainRoute";
import ErrorPage from "./pages/error/ErrorPage";
import axios from "axios";

axios.defaults.validateStatus = () => true

const router = createBrowserRouter([{
    id: 'index',
    path: '/',
    element: <IndexRoute/>,
    children: [{
        id: 'login',
        path: '/login',
        element: <LoginRoute/>
    }, {
        id: 'main',
        path: '/main',
        element: <MainRoute/>
    }, {
        id: 'error',
        path: '/error',
        element: <ErrorPage errorBody={{status: 404, type: 'NOT_FOUND', description: 'The requested resource is not found.', timestamp: 1696518924}}/>
    }]
}])

ReactDOM.createRoot(document.getElementById('app')!).render(<RouterProvider router={router}/>)
