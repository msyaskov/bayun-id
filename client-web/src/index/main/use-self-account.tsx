import {createContext, PropsWithChildren, useContext, useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";
import useErrorPage from "../../pages/error/use-error-page";
import Response from "../../api/Response";
import {createInternalError, createServiceUnavailableError, createUnknownError} from "../../api/ErrorBody";
import handle from "../../util/ResponseHandler";

// @ts-ignore
const SELF_PATH = import.meta.env.VITE_SELF_PATH

export type SelfAccountMutableData = {
    username: string
    firstName: string
    lastName: string
}

export type SelfAccount = SelfAccountMutableData & {
    id: string
    email: string
    pictureUrl: string
}

export type SelfAccountContextType = SelfAccount & {
    update: (self?: SelfAccount) => Promise<SelfAccount>
}

const SelfAccountContext = createContext<SelfAccountContextType | undefined>(undefined)

export const SelfAccountContextProvider = (props: PropsWithChildren) => {

    // @ts-ignore
    const [self, setSelf] = useState<SelfAccount>(null)

    async function update(sa?: SelfAccount): Promise<SelfAccount> {
        if (sa) {
            setSelf(sa)
            return sa
        }

        const ar = await axios.get<Response<SelfAccount>, AxiosResponse<Response<SelfAccount>>>(SELF_PATH)
        if (ar.request.responseURL.includes('/login')) {
            window.location.replace('/login')
        }

        return handle(ar, result => {
            setSelf(result)
            return result
        })
    }

    useEffect(() => {
        update()
    }, [])

    return <SelfAccountContext.Provider value={
        {update: update, ...self}
    }>
        {props.children}
    </SelfAccountContext.Provider>
}

const useSelfAccount = (): SelfAccountContextType => {
    const context = useContext<SelfAccountContextType | undefined>(SelfAccountContext)
    if (!context) {
        throw new Error("No SelfAccountContextProvider")
    }

    return context
}

export default useSelfAccount