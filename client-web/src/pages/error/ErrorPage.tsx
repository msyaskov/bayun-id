import ErrorBody from "../../api/ErrorBody";

const ErrorPage = (props: {errorBody: ErrorBody}) => {
    return <>
        <div className="hero min-h-screen">
            <div className="hero-content flex-col w-full gap-0">

                <div className="collapse w-fit">
                    <input type="checkbox" />
                    <div className='collapse-title flex flex-col items-center w-fit'>
                        <div>
                            <span className='text-9xl font-thin'>OOPS!</span>
                        </div>
                        <div className='-translate-y-12 h-0 w-fit'>
                            <div className='bg-white px-2 text-2xl font-medium'>
                                <span className='text-blue-600'><span className='text-red-600'>{props.errorBody.status} -</span>- {props.errorBody.type}</span>
                            </div>
                        </div>
                    </div>
                    <div className="collapse-content pl-6">
                        <div><span className='text-sm text-red-600'><span className='font-medium text-black'>Status:</span> {props.errorBody.status}</span></div>
                        <div><span className='text-sm text-blue-600'><span className='font-medium text-black'>Type:</span> {props.errorBody.type}</span></div>
                        <div><span className='text-sm text-green-600'><span className='font-medium text-black'>Description:</span> {props.errorBody.description}</span></div>
                        <div><span className='text-sm text-purple-600'><span className='font-medium text-black'>Timestamp:</span> {props.errorBody.timestamp} <span className='text-gray-500'>(unix)</span></span></div>
                    </div>
                </div>
                <a href="/" className="btn btn-ghost btn-outline w-xs normal-case text-lg font-normal w-full">На главную страницу</a>
            </div>
        </div>
    </>
}

export default ErrorPage