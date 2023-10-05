import {createContext, Dispatch, PropsWithChildren, SetStateAction, useContext, useEffect, useState} from "react";
import axios, {AxiosError, AxiosResponse} from "axios";
import config from "tailwindcss/defaultConfig";
import BaseResponse from "../../api/BaseResponse";

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
    update: () => Promise<SelfAccount>
}

const SelfAccountContext = createContext<SelfAccountContextType | undefined>(undefined)

export const SelfAccountContextProvider = (props: PropsWithChildren) => {

    // @ts-ignore
    const [self, setSelf] = useState<SelfAccount>(null)

    async function update(): Promise<SelfAccount> {
        console.log("update")

        // @ts-ignore
        const path = import.meta.env.VITE_SELF_PATH

        const response = await axios.get(path)

        return response.data.result;
    }

    useEffect(() => {
        console.log("effect")
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