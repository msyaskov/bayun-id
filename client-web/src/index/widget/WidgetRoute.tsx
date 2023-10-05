import {SelfAccountContextProvider} from "../main/use-self-account";
import Widget from "./Widget";

const WidgetRoute = () => {
    return <>
        <SelfAccountContextProvider>
            <Widget/>
        </SelfAccountContextProvider>
    </>
}

export default WidgetRoute