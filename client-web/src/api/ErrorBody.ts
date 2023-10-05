import ErrorType from "./ErrorType";

type ErrorBody = {
    status: number
    type: string
    description: string
    timestamp: number
    parameters?: string[]
}

export function errorCreator(status: number, type: ErrorType, description: string, timestamp?: number) {
    return {
        status: status,
        type: type,
        description: description,
        timestamp: timestamp ? timestamp : Date.now()
    }
}

export function createUnknownError() {
    return errorCreator(500, ErrorType.UNKNOWN, "Unknown error occurred.")
}

export function createInternalError() {
    return errorCreator(500, ErrorType.INTERNAL, "Internal server error.")
}

export function createServiceUnavailableError() {
    return errorCreator(503, ErrorType.SERVICE_UNAVAILABLE, "Service unavailable.")
}

export default ErrorBody