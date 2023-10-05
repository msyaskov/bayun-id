import {createContext, Dispatch, PropsWithChildren, SetStateAction, useContext, useEffect, useState} from "react";
import axios, {AxiosError, AxiosResponse} from "axios";
import config from "tailwindcss/defaultConfig";
import BaseResponse from "../../api/BaseResponse";
import ErrorBody from "../../api/ErrorBody";
import errorBody from "../../api/ErrorBody";
import ErrorPage from "./ErrorPage";

export type ErrorPageContextType = {
    show: (errorBody?: ErrorBody) => void
}

const ErrorPageContext = createContext<ErrorPageContextType | undefined>(undefined)

export const ErrorPageContextProvider = (props: PropsWithChildren) => {

    const [errorBody, setErrorBody] = useState<ErrorBody | undefined>(undefined)

    function show(errorBody?: ErrorBody) {
        setErrorBody(errorBody)
    }

    return <ErrorPageContext.Provider value={{show}}>
        {errorBody ? <ErrorPage errorBody={errorBody}/> : props.children}
    </ErrorPageContext.Provider>
}

const useErrorPage = (): ErrorPageContextType => {
    const context = useContext<ErrorPageContextType | undefined>(ErrorPageContext)
    if (!context) {
        throw new Error("No ErrorPageContextProvider")
    }

    return context
}

export default useErrorPage