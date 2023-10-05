const nameRegExp = new RegExp('^(?=.{1,30}$)(?!.*[ \\-.\',]{2})[a-zа-яеЁA-ZА-Я]+([a-zа-яеЁA-ZА-Я \\-.\',]*[a-zа-яеЁA-ZА-Я]+)*$')
const nameToSpecialsRegExp = new RegExp("^(?!.*[ \\-.',]{2}).+$")
const nameStartWithLetterRegExp = new RegExp('^[a-zа-яеЁA-ZА-Я]+.*$')
const nameEndWithLetterRegExp = new RegExp('^.*[a-zа-яеЁA-ZА-Я]+$')
const nameAlphabetRegExp = new RegExp('^[a-zа-яеЁA-ZА-Я \\-.\',]*$')
function nameQuick(name: string): string | undefined {
    if (name.length === 0) {
        return 'Обязательное поле'
    } else if (!nameRegExp.test(name)) {
        return 'Не подойдет'
    } else {
        return undefined
    }
}
function nameInline(name: string): string | undefined {
    if (name.length === 0) {
        return undefined
    } else if (30 < name.length) {
        return 'Укажите сокращенный вариант'
    } else if (!nameAlphabetRegExp.test(name)) {
        return 'Используйте кириллицу или латиницу, а так же символы: - \' , .'
    } else if (!nameToSpecialsRegExp.test(name)) {
        return 'Не используйте два специальных символа подряд'
    } else if (!nameStartWithLetterRegExp.test(name)) {
        return 'Начните с буквы'
    } else if (!nameEndWithLetterRegExp.test(name)) {
        return 'Закончите буквой'
    } else {
        return undefined
    }
}
function nameFull(name: string): string | undefined {
    if (name.length === 0) {
        return 'Обязательное поле'
    } else {
        return nameInline(name)
    }
}

const usernameRegExp = new RegExp("^(?!.*_{2}.*)[a-zA-Zа-яёЁА-Я\\d_]{1,30}$")
const usernameToUnderlinesRexExp = new RegExp("^(?!.*_{2}.*).*$")
const usernameAlphabetRegExp = new RegExp('^[a-zA-Zа-яёЁА-Я\\d_]+$')
function usernameQuick(username: string): string | undefined {
    if (username.length === 0) {
        return 'Обязательное поле'
    } else if (!usernameRegExp.test(username)) {
        return 'Не подойдет'
    } else {
        return undefined
    }
}
function usernameInline(username: string): string | undefined {
    if (username.length === 0) {
        return undefined
    } else if (30 < username.length) {
        return 'Никнейм слишком длинный'
    } else if (!usernameToUnderlinesRexExp.test(username)) {
        return 'Не используйте два подчеркивания подряд'
    } else if (!usernameAlphabetRegExp.test(username)) {
        return 'Используйте кириллицу или латиницу, цифры и знак подчеркивания'
    } else {
        return undefined
    }
}
function usernameFull(username: string): string | undefined {
    if (username.length === 0) {
        return 'Обязательное поле'
    } else {
        return usernameInline(username)
    }
}

export default {
    name: {
        quick: nameQuick,
        inline: nameInline,
        full: nameFull
    },
    username: {
        quick: usernameQuick,
        inline: usernameInline,
        full: usernameFull
    }
}