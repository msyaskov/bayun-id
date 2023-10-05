import useSelfAccount, {SelfAccountContextType} from "../main/use-self-account";
import axios from "axios";

const Widget = (props: {self?: SelfAccountContextType}) => {

    const self = props.self ? props.self : useSelfAccount()

    function onLogOut() {
        axios.post('/api/logout')
    }

    return <>
        <div className="card w-xs">
            <div className="card-body pt-4 pb-6 px-6">
                <div className='text-center'>
                    <span className='font-medium'>{self.email}</span>
                </div>
                <div className='flex w-full gap-4 p-4 h-24 rounded-t-3xl'>
                    <div className="avatar">
                        <div className="w-16 mask mask-squircle">
                            <img src={self.pictureUrl} alt=''/>
                        </div>
                    </div>
                    <div className='-space-y-2 mt-3'>
                        <div>
                            <span className='text-lg font-medium'>{self.firstName} {self.lastName}</span>
                        </div>
                        <div>
                            <span className='text-sm text-gray-500'>@{self.username}</span>
                        </div>
                    </div>
                </div>
                <div className='grid grid-cols-2 gap-1'>
                    <a href='/main' className='btn btn-outline btn-neutral col-span-2 rounded-b-none w-full'>Управление аккаунтом</a>
                    <a href='/login' className='btn btn-outline btn-neutral rounded-t-none rounded-r-none'>Войти в другой аккаунт</a>
                    <button className='btn btn-outline btn-neutral rounded-t-none rounded-l-none' onClick={onLogOut}>Выход</button>
                </div>
            </div>
        </div>
    </>
}

export default Widget