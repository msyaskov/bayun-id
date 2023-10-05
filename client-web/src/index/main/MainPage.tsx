import useSelfAccount, {SelfAccount, SelfAccountContextType} from "./use-self-account";
import {SvgGoogleLogo, SvgLogo, SvgYandexLogo} from "../../components/svg";
import {ChangeEvent, useCallback, useEffect, useState} from "react";
import axios, {AxiosError, AxiosResponse} from "axios";
import validation from "../../api/validation";
import BaseResponse from "../../api/BaseResponse";
import ErrorBody, {createUnknownError, errorCreator} from "../../api/ErrorBody";
import ErrorResponse from "../../api/ErrorResponse";
import ResultResponse from "../../api/ResultResponse";
import Response from "../../api/Response";
import useErrorPage from "../../pages/error/use-error-page";
import handle from "../../util/ResponseHandler";
import ErrorType from "../../api/ErrorType";

const MainSkeletonPage = () => {
    return <>
        skeleton
    </>
}

const MainPageSelf = (props: {self: SelfAccountContextType}) => {

    const ErrorPage = useErrorPage()

    const [username, setUsername] = useState<string>(props.self.username)
    const [usernameError, setUsernameError] = useState<string | undefined>(undefined)
    function onChangeUsername(e: ChangeEvent<HTMLInputElement>) {
        const value = e.target.value.trim()
        setUsername(value)

        const validationResult = validation.username.inline(value)
        setUsernameError(validationResult)
    }

    const [firstName, setFirstName] = useState<string>(props.self.firstName)
    const [firstNameError, setFirstNameError] = useState<string | undefined>(undefined)
    function onChangeFirstName(e: ChangeEvent<HTMLInputElement>) {
        const value = e.target.value
        setFirstName(value)

        const validationResult = validation.name.inline(value)
        setFirstNameError(validationResult)
    }

    const [lastName, setLastName] = useState<string>(props.self.lastName)
    const [lastNameError, setLastNameError] = useState<string | undefined>(undefined)
    function onChangeLastName(e: ChangeEvent<HTMLInputElement>) {
        const value = e.target.value
        setLastName(value)

        const validationResult = validation.name.inline(value)
        setLastNameError(validationResult)
    }

    function isSubmitDisabled() {
        return props.self.username === username
                && props.self.firstName === firstName
                && props.self.lastName === lastName
    }

    function onLogOut() {
        axios.post('/api/logout').then(() => {
            window.location.replace('/login')
        })
    }

    async function onSave() {
        const usernameValidationResult = validation.username.full(username)
        const firstNameValidationResult = validation.name.full(firstName)
        const lastNameValidationResult = validation.name.full(lastName)

        if (usernameValidationResult || firstNameValidationResult || lastNameValidationResult) {
            setUsernameError(usernameValidationResult)
            setFirstNameError(firstNameValidationResult)
            setLastNameError(lastNameValidationResult)
            return
        }

        let data = {username, firstName, lastName}

        // @ts-ignore
        const path = import.meta.env.VITE_SELF_PATH

        const ar = await axios.patch<Response, AxiosResponse<Response>>(path, data, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })

        handle(ar, result => {
            setUsername(result.username)
            setFirstName(result.firstName)
            setLastName(result.lastName)
            props.self.update()
        }, ErrorPage, errorBody => {
            if (errorBody.type === ErrorType.INVALID_REQUEST_PARAM && errorBody.parameters) {
                for (let param of errorBody.parameters) {
                    if (param === 'username') {
                        setUsernameError('Не подойдет')
                    } else if (param === 'firstName') {
                        setFirstNameError('Не подойдет')
                    } else if (param === 'lastName') {
                        setLastNameError('Не подойдет')
                    }
                }
            }
            return false
        })
    }

    return <>
        <div className="hero min-h-screen">
            <div className="hero-content flex-col">
                <div className='w-full sm:w-xs flex flex-col items-center sm:border sm:rounded-3xl sm:shadow-lg divide-y'>
                    <div className='flex w-full gap-4 p-4 h-24 rounded-t-3xl'>
                        <div className="avatar">
                            <div className="w-16 mask mask-squircle">
                                <img src={props.self.pictureUrl} alt=''/>
                            </div>
                        </div>
                        <div className='-space-y-2 mt-3'>
                            <div>
                                <span className='text-lg font-medium'>{props.self.firstName} {props.self.lastName}</span>
                            </div>
                            <div>
                                <span className='text-sm text-gray-500'>@{props.self.username}</span>
                            </div>
                        </div>

                    </div>
                    <div className='grid grid-cols-1 w-full p-4 gap-4'>
                        <div className="form-control">
                            <label className="label">
                                <span className="label-text">Имя</span>
                            </label>
                            <input type="text" value={firstName} onChange={onChangeFirstName} placeholder="Введите ваше имя" className="input input-bordered"/>
                            {firstNameError && <label className="label">
                                <span className="label-text-alt text-sm text-error">{firstNameError}</span>
                            </label>}
                        </div>
                        <div className="form-control">
                            <label className="label">
                                <span className="label-text">Фамилия</span>
                            </label>
                            <input type="text" value={lastName} onChange={onChangeLastName} placeholder="Введите вашу фамилию" className="input input-bordered"/>
                            {lastNameError && <label className="label">
                                <span className="label-text-alt text-sm text-error">{lastNameError}</span>
                            </label>}
                        </div>
                        <div className="form-control">
                            <label className="label">
                                <span className="label-text">Никнейм</span>
                            </label>
                            <input type="text" value={username} onChange={onChangeUsername} placeholder="Введите ваш никнейм" className="input input-bordered"/>
                            {usernameError && <label className="label">
                                <span className="label-text-alt text-sm text-error">{usernameError}</span>
                            </label>}
                        </div>
                        <div className='grid grid-cols-2 gap-2'>
                            <button className="btn btn-ghost text-error btn-outline hover:bg-error hover:border-error hover:text-white normal-case text-lg font-normal w-full rounded-r-none" onClick={onLogOut}>Выход</button>
                            <button className="btn btn-neutral normal-case font-normal text-lg rounded-l-none" disabled={isSubmitDisabled()} onClick={onSave}>Сохранить</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </>
}

const MainPage = () => {

    const self = useSelfAccount()

    return self.id == null ? <MainSkeletonPage/> : <MainPageSelf self={self}/>
}

export default MainPage