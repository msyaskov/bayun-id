import {SelfAccountContextProvider} from "./use-self-account";
import MainPage from "./MainPage";
import {ErrorPageContextProvider} from "../../pages/error/use-error-page";

const MainRoute = () => {
    return <>
        <ErrorPageContextProvider>
            <SelfAccountContextProvider>
                <MainPage/>
            </SelfAccountContextProvider>
        </ErrorPageContextProvider>
    </>
}

export default MainRoute