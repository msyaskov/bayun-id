type BaseResponse<T = any> = {
    ok: boolean
    result: T
}

export default BaseResponse