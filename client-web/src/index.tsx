import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import IndexRoute from "./index/IndexRoute";
import LoginRoute from "./index/login/LoginRoute";
import MainRoute from "./index/main/MainRoute";
import axios from "axios";
import WidgetRoute from "./index/widget/WidgetRoute";

axios.defaults.validateStatus = () => true

const csrfToken = document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content
console.log('_csrf', csrfToken)
axios.defaults.headers.post['X-CSRF-TOKEN'] = csrfToken
axios.defaults.headers.patch['X-CSRF-TOKEN'] = csrfToken

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
        id: 'widget',
        path: '/widget',
        element: <WidgetRoute/>
    }]
}])

ReactDOM.createRoot(document.getElementById('app')!).render(<RouterProvider router={router}/>)
