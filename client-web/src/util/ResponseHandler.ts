import {AxiosResponse} from "axios";
import ErrorBody, {createServiceUnavailableError, createUnknownError} from "../api/ErrorBody";
import {ErrorPageContextType} from "../pages/error/use-error-page";
import Response from "../api/Response";

export default async function handle(ar: AxiosResponse<Response<any>>,
                               resultHandler?: (result: any) => any, ErrorPage?: ErrorPageContextType, errorHandler?: (errorBody: ErrorBody) => boolean) {
    console.log('ar', ar)
    // @ts-ignore
    if (ar.status === 500 && ar.data === '') {
        ErrorPage?.show(createServiceUnavailableError())
        return
    }

    if (ar.data.ok && resultHandler) {
        return resultHandler(ar.data.result)
    }

    const error = ar.data.error
    if (!error) {
        ErrorPage?.show(createUnknownError())
        return
    }

    if (!errorHandler || !errorHandler(error)) {
        ErrorPage?.show(error)
    }
}