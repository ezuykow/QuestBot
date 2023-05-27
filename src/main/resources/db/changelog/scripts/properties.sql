-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE properties
(
    key VARCHAR(100) PRIMARY KEY,
    description TEXT NOT NULL,
    actual_property TEXT NOT NULL,
    default_property TEXT NOT NULL
);

-- changeset ezuykow:2
INSERT INTO properties VALUES
    (
        'viewer.questions.page.size',
        'Количество вопросов, отображаемых на одной странице по команде /showquestions',
        '5',
        '5'
    );
INSERT INTO properties VALUES
    (
        'answers.descriptor',
        'Разделитель вариантов ответа в файле Excel с вопросами',
        '!',
        '!'
    );
INSERT INTO properties VALUES
    (
        'messages.welcome',
        'Сообщение, отправляемое ботом при команде /start',
        'Привет, я QuestBot) Если вы хотите назначить этот чат админским - выберите соответсвующую команду в меню',
        'Привет, я QuestBot) Если вы хотите назначить этот чат админским - выберите соответсвующую команду в меню'
    );
INSERT INTO properties VALUES
    (
        'messages.startUp',
        'Сообщение, отправляемое ботом при его запуске',
        'Бот запущен и слушает все, что вы скажете🧐',
        'Бот запущен и слушает все, что вы скажете🧐'
    );
INSERT INTO properties VALUES
    (
        'messages.stopBot',
        'Сообщение, отправляемое ботом при его остановке',
        'Бот остановлен, больше не следит за вами🥹',
        'Бот остановлен, больше не следит за вами🥹'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.chatIsAdminNow',
        'Сообщение, отправляемое ботом, при назначении чата админским',
        'Теперь этот чат - администраторский',
        'Теперь этот чат - администраторский'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.chatIsGlobalNow',
        'Сообщение, отправляемое ботом, при назначении чата не админским',
        'Теперь этот чат - не администраторский',
        'Теперь этот чат - не администраторский'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.emptyPromotionList',
        'Сообщение, отправляемое ботом, когда хозяин вызвал команду /promote, но назначить админом некого',
        'Некого назначать админом',
        'Некого назначать админом'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.promote',
        'Сообщение, отправляемое ботом, когда хозяин вызвал команду /promote',
        'Назначить администратором бота:',
        'Назначить администратором бота:'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.demote',
        'Сообщение, отправляемое ботом, когда хозяин вызвал команду /demote',
        'Разжаловать администратора бота:',
        'Разжаловать администратора бота:'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.userPromoted',
        'Сообщение, отправляемое ботом, когда хозяин назначил нового администратора',
        ' назначен администратором бота',
        ' назначен администратором бота'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.userDemoted',
        'Сообщение, отправляемое ботом, когда хозяин разжаловал администратора',
        ' разжалован из администраторов бота',
        ' разжалован из администраторов бота'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.validation.startCmdFailed',
        'Сообщение, отправляемое ботом, когда вызвана команда /start, но чат уже был добавлен в систему',
        'Этот чат уже добавлен в систему',
        'Этот чат уже добавлен в систему'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.validation.adminOnCmdFailed',
        'Сообщение, отправляемое ботом, когда вызвана команда /adminon, но чат уже администраторский',
        'Этот чат уже администраторский',
        'Этот чат уже администраторский'
    );
INSERT INTO properties VALUES
    (
        'messages.owner.validation.chatIsNotAdmin',
        'Сообщение, отправляемое ботом, если вызвана команда /adminoff, /promote или /demote в не администраторском чате',
        'Этот чат не администраторский',
        'Этот чат не администраторский'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.invalidMsg',
        'Сообщение, отправляемое ботом, если вызвана неверная команда',
        'Введена неверная команда',
        'Введена неверная команда'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.cmdSendByNotAdmin',
        'Сообщение, отправляемое ботом, если не админом вызвана админская команда',
        'Эту команду может использовать только админ бота',
        'Эту команду может использовать только админ бота'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.adminCmdInGlobalChat',
        'Сообщение, отправляемое ботом, если в общем чате вызвана команда, которую можно вызывать только в админском чате',
        'Эту команду можно использовать только в администраторском чате',
        'Эту команду можно использовать только в администраторском чате'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.gameCmdInAdminChat',
        'Сообщение, отправляемое ботом, если в админском чате вызвана игровая команда',
        'В админском чате оставлена игровая команда',
        'В админском чате оставлена игровая команда'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.isOwnerCommand',
        'Сообщение, отправляемое ботом, если команда владельца вызвана не владельцем',
        'Эту команду может использовать только владелец',
        'Эту команду может использовать только владелец'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.startGameCreating',
        'Сообщение, отправляемое ботом, когда администратор начал создавать новую игру',
        ' начал создавать новую, остальные пользователи временно заблокированы',
        ' начал создавать новую, остальные пользователи временно заблокированы'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.endGameCreating',
        'Сообщение, отправляемое ботом, когда администратор закончил создавать новую игру',
        'Создание новой игры закончено, пользователи разблокированы',
        'Создание новой игры закончено, пользователи разблокированы'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.startQuestionView',
        'Сообщение, отправляемое ботом, когда администратор начал работать с вопросами',
        ' начал работать с вопросами, остальные пользователи временно заблокированы',
        ' начал работать с вопросами, остальные пользователи временно заблокированы'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.endQuestionView',
        'Сообщение, отправляемое ботом, когда администратор закончил работать с вопросами',
        'Работа с вопросами закончена, пользователи разблокированы',
        'Работа с вопросами закончена, пользователи разблокированы'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.startGamesView',
        'Сообщение, отправляемое ботом, когда администратор начал работать со списком игр',
        ' начал работать со списком игр, остальные пользователи временно заблокированы',
        ' начал работать со списком игр, остальные пользователи временно заблокированы'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.endGamesView',
        'Сообщение, отправляемое ботом, когда администратор закончил работать со списком игр',
        'Работа со списком игр закончена, пользователи разблокированы',
        'Работа со списком игр закончена, пользователи разблокированы'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.cmdForGlobalChat',
        'Сообщение, отправляемое ботом, когда в админском чате оставлена команда /deletechat или /preparegame',
        'Эту команду можно использовать только в не администраторском чате',
        'Эту команду можно использовать только в не администраторском чате'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.chatNotInGame',
        'Сообщение, отправляемое ботом, когда чат удаляется из системы',
        'Этот чат больше не в Игре',
        'Этот чат больше не в Игре'
    );
INSERT INTO properties VALUES
    (
        'messages.admins.choosePreparingGame',
        'Сообщение, отправляемое ботом, когда вызвана команда /preparegame',
        ', выберите игру, которую нужно подготовить:',
        ', выберите игру, которую нужно подготовить:'
    );
INSERT INTO properties VALUES
    (
        'messages.players.haventStartedGame',
        'Сообщение, отправляемое ботом, когда вызвана команда /questions, но игра в чате не запущена',
        'Запущенных игр нет',
        'Запущенных игр нет'
    );
INSERT INTO properties VALUES
    (
        'messages.players.enterTeamName',
        'Сообщение, отправляемое ботом, когда вызвана команда /regteam при подготовке к игре',
        'В ответ на это сообщение введите название своей команды',
        'В ответ на это сообщение введите название своей команды'
    );
INSERT INTO properties VALUES
    (
        'messages.players.noTeamsRegisteredYet',
        'Сообщение, отправляемое ботом, когда вызвана команда /join при подготовке к игре, но вступать пока некуда',
        'Пока не зарегистрировано ни одной команды',
        'Пока не зарегистрировано ни одной команды'
    );
INSERT INTO properties VALUES
    (
        'messages.players.chooseYourTeam',
        'Сообщение, отправляемое ботом, когда вызвана команда /join при подготовке к игре',
        ', выберите команду, в которую хотите вступить',
        ', выберите команду, в которую хотите вступить'
    );
INSERT INTO properties VALUES
    (
        'messages.members.welcomeAdminPrefix',
        'Префикс сообщения, отправляемого ботом, когда в админский чат вступил новый помощник',
        'Новый помощник - ',
        'Новый помощник - '
    );
INSERT INTO properties VALUES
    (
        'messages.members.welcomeAdminSuffix',
        'Суффикс сообщения, отправляемого ботом, когда в админский чат вступил новый помощник',
        ', приветствуем!',
        ', приветствуем!'
    );
INSERT INTO properties VALUES
    (
        'messages.members.byeAdmin',
        'Сообщение, отправляемое ботом, когда админский чат покинул помощник',
        ' покинул нас...',
        ' покинул нас...'
    );
INSERT INTO properties VALUES
    (
        'messages.documents.emptyQuestionList',
        'Сообщение, отправляемое ботом, когда в Excel файле не обнаружено вопросов',
        'Вопросов в файле не обнаружено!',
        'Вопросов в файле не обнаружено!'
    );
INSERT INTO properties VALUES
    (
        'messages.documents.fromNotAdmin',
        'Сообщение, отправляемое ботом, когда Excel файле отправлен не администратором',
        'Добавлять вопросы может только администратор',
        'Добавлять вопросы может только администратор'
    );
INSERT INTO properties VALUES
    (
        'messages.documents.wrongDocumentType',
        'Сообщение, отправляемое ботом, когда отправленный файл не является Excel-файлом',
        'Поддерживается добавление вопросов только с файла Excel',
        'Поддерживается добавление вопросов только с файла Excel'
    );
INSERT INTO properties VALUES
    (
        'messages.documents.emptyQuestionsNotAdded',
        'Сообщение, отправляемое ботом, когда в Excel-файле были вопросы без текста или ответа',
        'Пустые вопросы не добавлены! (Без текста вопроса или ответа)\n\n',
        'Пустые вопросы не добавлены! (Без текста вопроса или ответа)\n\n'
    );
INSERT INTO properties VALUES
    (
        'messages.documents.equalsQuestionsUpdated',
        'Сообщение, отправляемое ботом, когда в Excel-файле были повторяющиеся вопросы',
        'Повторяющиеся вопросы отредактированы!\n\n',
        'Повторяющиеся вопросы отредактированы!\n\n'
    );
INSERT INTO properties VALUES
    (
        'messages.documents.questionsAdded',
        'Сообщение, отправляемое ботом, когда с Excel-файла были добавлены вопросы',
        'Добавлены вопросы (%d):\n\n',
        'Добавлены вопросы (%d):\n\n'
    );
INSERT INTO properties VALUES
    (
        'messages.documents.noOneQuestionAdded',
        'Сообщение, отправляемое ботом, когда с Excel-файла не было добавлено ни одного вопроса',
        'Ни одного вопроса не добавлено',
        'Ни одного вопроса не добавлено'
    );
INSERT INTO properties VALUES
    (
        'messages.documents.defaultQuestionGroup',
        'Группа, в которую будут добавлены вопросы, у которых она не указана',
        'Вопросы без группы',
        'Вопросы без группы'
    );
INSERT INTO properties VALUES
    (
        'messages.questions.emptyList',
        'Сообщение, отправляемое ботом, если вызвана команда /showquestions, а в базе нет вопросов',
        '🔴В базе нет вопросов',
        '🔴В базе нет вопросов'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestNewGameName',
        'Сообщение, отправляемое ботом, для запроса имени новой игры',
        '🟡Введите название новой игры',
        '🟡Введите название новой игры'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestQuestionsGroups',
        'Сообщение, отправляемое ботом, для запроса групп вопросов для новой игры',
        '🟢Название игры - "%s"\n🟡Выберите группы вопросов, которые включить в игру:',
        '🟢Название игры - "%s"\n🟡Выберите группы вопросов, которые включить в игру:'
    );
INSERT INTO properties VALUES
    (
        'messages.games.addedQuestionGroup',
        'Сообщение, отправляемое ботом, для запроса групп вопросов для новой игры, когда одна или несколько групп уже выбраны',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟡Выберите группы вопросов, которые включить в игру:',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟡Выберите группы вопросов, которые включить в игру:'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestMaxQuestionsCount',
        'Сообщение, отправляемое ботом, для запроса максимального количества вопросов для новой игры',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟡Введите максимальное количество вопросов на игру',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟡Введите максимальное количество вопросов на игру'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestStartCountTasks',
        'Сообщение, отправляемое ботом, для запроса стартового количества вопросов для новой игры',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟡Введите количество вопросов, которые будут сразу доступны игрокам при старте игры',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟡Введите количество вопросов, которые будут сразу доступны игрокам при старте игры'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestMaxPerformedQuestionCount',
        'Сообщение, отправляемое ботом, для запроса количества вопросов для досрочной победы одной из команд для новой игры',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟡Введите количество вопросов, при ответе на которое команда становится победителем досрочно',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟡Введите количество вопросов, при ответе на которое команда становится победителем досрочно'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestMinQuestionsCountInGame',
        'Сообщение, отправляемое ботом, для запроса количества вопросов при котором нужно добавлять еще вопросы для новой игры',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟡Введите количество вопросов, при котором к активным вопросом добавляются новые',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟡Введите количество вопросов, при котором к активным вопросом добавляются новые'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestQuestionsCountToAdd',
        'Сообщение, отправляемое ботом, для запроса количества вопросов которые нужно добавлять при достижении минимального порога для новой игры',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟡Введите количество вопросов, которое нужно добавлять при достижении порога',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟡Введите количество вопросов, которое нужно добавлять при достижении порога'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestMaxTimeMinutes',
        'Сообщение, отправляемое ботом, для запроса количества времени в минутах на игру для новой игры',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟢Количество добавляемых вопросов: %d\n🟡Введите время проведения игры в минутах',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟢Количество добавляемых вопросов: %d\n🟡Введите время проведения игры в минутах'
    );
INSERT INTO properties VALUES
    (
        'messages.games.requestAdditionWithTask',
        'Сообщение, отправляемое ботом, для запроса необходимости показывать доп. информацию с самим вопросом или с ответом для новой игры',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟢Количество добавляемых вопросов: %d\n🟢Время проведения игры в минутах: %d\n🟡Введите 0, если дополнительную информацию нужно показывать вместе с вопросом, или 1, если при правильном ответе на вопрос',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟢Количество добавляемых вопросов: %d\n🟢Время проведения игры в минутах: %d\n🟡Введите 0, если дополнительную информацию нужно показывать вместе с вопросом, или 1, если при правильном ответе на вопрос'
    );
INSERT INTO properties VALUES
    (
        'messages.games.gameAdded',
        'Сообщение, отправляемое ботом, когда новая игра успешно создана',
        '🎉Новая игра добавлена!\n🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟢Количество добавляемых вопросов: %d\n🟢Время проведения игры в минутах: %d\n🟢Доп. информация с вопросом: %s',
        '🎉Новая игра добавлена!\n🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟢Количество добавляемых вопросов: %d\n🟢Время проведения игры в минутах: %d\n🟢Доп. информация с вопросом: %s'
    );
INSERT INTO properties VALUES
    (
        'messages.games.gameInfo',
        'Сообщение, отправляемое ботом, когда запрошена информация об игре',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟢Количество добавляемых вопросов: %d\n🟢Время проведения игры в минутах: %d\n🟢Доп. информация с вопросом: %s',
        '🟢Название игры - "%s"\n🟢Добавленные группы: \n%s\n🟢Максимальное количество вопросов: %d\n🟢Стартовое количество вопросов: %d\n🟢Количество вопросов для победы: %d\n🟢Порог активных вопросов: %d\n🟢Количество добавляемых вопросов: %d\n🟢Время проведения игры в минутах: %d\n🟢Доп. информация с вопросом: %s'
    );
INSERT INTO properties VALUES
    (
        'messages.games.nameAlreadyTaken',
        'Сообщение, добавляемое ботом к запросу имени новой игры, когда введенное имя уже занято',
        '🔴Название "%s" уже занято\n',
        '🔴Название "%s" уже занято\n'
    );
INSERT INTO properties VALUES
    (
        'messages.games.invalidNumber',
        'Сообщение, добавляемое ботом к запросу параметра новой игры, когда введенное число некорректно',
        '🔴Глупости, попробуйте еще раз!\n',
        '🔴Глупости, попробуйте еще раз!\n'
    );
INSERT INTO properties VALUES
    (
        'messages.games.invalidQuestionCount',
        'Сообщение, добавляемое ботом к запросу параметра новой игры, когда введенное число вопросов превышает количество вопросов в базе',
        '🔴Такого количества вопросов нет, попробуйте еще раз!\n',
        '🔴Такого количества вопросов нет, попробуйте еще раз!\n'
    );
INSERT INTO properties VALUES
    (
        'messages.games.startQMoreMaxQ',
        'Сообщение, добавляемое ботом к запросу параметра новой игры, когда введенное число стартовых вопросов превышает количество вопросов на игру',
        '🔴Стартовое количество вопросов не может быть больше максимального, попробуйте еще раз!\n',
        '🔴Стартовое количество вопросов не может быть больше максимального, попробуйте еще раз!\n'
    );
INSERT INTO properties VALUES
    (
        'messages.games.maxPerformedQMoreMaxQ',
        'Сообщение, добавляемое ботом к запросу параметра новой игры, когда введенное число вопросов для досрочной победы превышает количество вопросов на игру',
        '🔴Количество вопросов до победы не может быть больше максимального, попробуйте еще раз!\n',
        '🔴Количество вопросов до победы не может быть больше максимального, попробуйте еще раз!\n'
    );
INSERT INTO properties VALUES
    (
        'messages.games.emptyGamesList',
        'Сообщение, отправляемое ботом, когда вызвана команда /showgames или /preparegame, но в базе нет игр',
        '🔴Ни одной игры не создано',
        '🔴Ни одной игры не создано'
    );
INSERT INTO properties VALUES
    (
        'messages.games.failedDeletingGame',
        'Сообщение, отправляемое ботом, когда админ пытается удалить игру, которая проводится в данный момент',
        '🔴Нельзя удалить запущенную игру!',
        '🔴Нельзя удалить запущенную игру!'
    );
INSERT INTO properties VALUES
    (
        'messages.games.notEnoughQuestions',
        'Сообщение, отправляемое ботом, когда админ пытается подготовить игру, для которой не хватает вопросов в базе',
        '🔴Для проведения этой игры нехватает вопросов в базе!',
        '🔴Для проведения этой игры нехватает вопросов в базе!'
    );
INSERT INTO properties VALUES
    (
        'messages.games.prepareInterrupted',
        'Сообщение, отправляемое ботом, когда админ прервал подготовку игры',
        ' прервал подготовку игры, команды расформированы!🔴',
        ' прервал подготовку игры, команды расформированы!🔴'
    );
INSERT INTO properties VALUES
    (
        'messages.games.gameStarted',
        'Сообщение, отправляемое ботом, когда админ запустил игру',
        ' запустил игру!',
        ' запустил игру!'
    );
INSERT INTO properties VALUES
    (
        'messages.games.prepareGameStartedHint',
        'Сообщение, отправляемое ботом, когда админ запустил подготовку к игре',
        '🎲Началась подготовка к игре "%s"!🎲\n\n®️ Для регистрации новой команды выберите в меню команду /regteam\n✔️ Для вступления в команду выберите в меню команду /jointeam\n\n▶️ Когда все будут готовы, администратор запустит игру',
        '🎲Началась подготовка к игре "%s"!🎲\n\n®️ Для регистрации новой команды выберите в меню команду /regteam\n✔️ Для вступления в команду выберите в меню команду /jointeam\n\n▶️ Когда все будут готовы, администратор запустит игру'
    );
INSERT INTO properties VALUES
    (
        'messages.games.gameStartedHint',
        'Сообщение-подсказка игрокам, отправляемое ботом, когда админ запустил игру',
        '🎲Игра "%s" запущена!🎲\n\n⏰ Время проведения игры - %d минут\n✔️ Досрочная победа при ответе на %d вопроса(-ов)\n\n❔Сейчас вы получите список вопросов, который будет закреплен в чате❔\n✍️ Для ответа на вопрос введите "<№ вопроса> <ответ>", например "1 собака"\n🍀 Удачной игры! 🍀',
        '🎲Игра "%s" запущена!🎲\n\n⏰ Время проведения игры - %d минут\n✔️ Досрочная победа при ответе на %d вопроса(-ов)\n\n❔Сейчас вы получите список вопросов, который будет закреплен в чате❔\n✍️ Для ответа на вопрос введите "<№ вопроса> <ответ>", например "1 собака"\n🍀 Удачной игры! 🍀'
    );
INSERT INTO properties VALUES
    (
     'messages.games.time',
     'Сообщение-подсказка игрокам, отправляемое ботом, в котором показывается оставшееся время на игру',
     '❗️❗️❗️ До конца игры осталось %d минут ❗️❗️❗️',
     '❗️❗️❗️ До конца игры осталось %d минут ❗️❗️❗️'
    )