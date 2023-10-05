import {Outlet} from "react-router";
import {SelfAccountContextProvider} from "./use-self-account";
import MainPage from "./MainPage";

const MainRoute = () => {
    return <>
        <SelfAccountContextProvider>
            <MainPage/>
        </SelfAccountContextProvider>
    </>
}

export default MainRoute