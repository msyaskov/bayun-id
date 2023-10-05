import ErrorBody from "./ErrorBody";

type Response<R = any> = {
    ok: boolean
    result?: R
    error?: ErrorBody
}

export default Response