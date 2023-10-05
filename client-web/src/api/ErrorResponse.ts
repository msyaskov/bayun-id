import ErrorBody from "./ErrorBody";
import BaseResponse from "./BaseResponse";

type ErrorResponse = {
    ok: false
    error: ErrorBody
}

export default ErrorResponse