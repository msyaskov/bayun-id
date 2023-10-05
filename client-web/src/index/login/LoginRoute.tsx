import {SvgGoogleLogo, SvgLogo, SvgYandexLogo} from "../../components/svg";
import { Helmet, HelmetProvider } from 'react-helmet-async';

// @ts-ignore
const OAuth2Clients = import.meta.env.VITE_OAUTH2_CLIENTS.split(',')

const LoginRoute = () => {
    return <>
        <HelmetProvider>
            <Helmet>
                <title>Login - Bayun</title>
            </Helmet>
        </HelmetProvider>
        <div className="hero min-h-screen">
            <div className="hero-content w-full xs:w-xs flex flex-col items-center sm:border sm:rounded-3xl sm:shadow-lg pt-8 px-4 pb-4 gap-6">
                    <SvgLogo className='w-40 h-40'/>
                    <div className=''>
                        <div className='text-center mb-4'>
                            <span className='text-3xl font-medium'>Вход в Bayun</span>
                        </div>
                        <div className='space-y-4 w-full p-4'>
                            {OAuth2Clients.includes('google') && <>
                                <button className='w-full btn btn-outline normal-case text-base font-medium'
                                    onClick={() => window.location.replace('/oauth2/authorization/google')}>
                                    <SvgGoogleLogo className='w-6 h-6'/>
                                    Войти с помощью Google
                                </button>
                            </>}
                            {OAuth2Clients.includes('yandex') && <>
                                <button className='w-full btn btn-outline normal-case text-base font-medium'
                                        onClick={() => window.location.replace('/oauth2/authorization/yandex')}>
                                    <SvgYandexLogo className='w-6 h-6'/>
                                    Войти с помощью Yandex
                                </button>
                            </>}
                        </div>
                    </div>
            </div>
        </div>
    </>
}

export default LoginRoute