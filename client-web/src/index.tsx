import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import IndexRoute from "./index/IndexRoute";
import LoginRoute from "./index/login/LoginRoute";
import MainRoute from "./index/main/MainRoute";

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
    }]
}])

ReactDOM.createRoot(document.getElementById('app')!).render(<RouterProvider router={router}/>)
