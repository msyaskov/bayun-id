import BaseResponse from "./BaseResponse";

type ResultResponse<T = any> = {
    ok: true
    result: T
}

export default ResultResponse