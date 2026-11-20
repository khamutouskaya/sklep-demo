# Sklep — передача работы в новый чат

## Мини-скрипт: вставьте этот текст первым сообщением в новом чате

```
Продолжаем работу над моим проектом Sklep: C:\Users\User\IdeaProjects\Sklep
Сначала прочитай файлы HANDOFF.md и PLAN.md в папке проекта — там правила, текущий статус и план.
Отвечай по-русски. Код пишешь ты, я проверяю.
Правила: не делай git commit/push, не добавляй комментарии в код, объясняй изменения в чате.
Продолжи с шага, отмеченного «СЛЕДУЮЩИЙ ШАГ» в HANDOFF.md.
```

---

## О проекте

- **Sklep** — десктопный интернет-магазин одежды: JavaFX 23 (FXML, Scene Builder) + PostgreSQL (JDBC) + Maven, Java 21.
- Три роли: гость (`lobby`), клиент (`klient`), администратор (`admin`).
- Цель: довести проект до уровня портфолио и выложить на GitHub, чтобы работодатели оценили навыки.
- Выбран **путь A**: оставить JavaFX, но сделать профессионально (Docker для БД, слой сервисов, BCrypt, навигация, CSS, тесты, README).

## Правила работы

1. Код пишет Claude, пользователь проверяет.
2. **Не делать git commit и push.** Коммиты пользователь делает сам.
3. **Не добавлять комментарии в код.** Пояснения — в чате.
4. Общение на русском; объяснять, что изменено и зачем, таблицей «файл → что → зачем».
5. Код на английском, тексты интерфейса на польском.
6. После изменений проверять сборку: `mvnw.cmd -q compile`, запуск `mvnw.cmd javafx:run`.
7. Работать по одному этапу из `PLAN.md`, после этапа останавливаться и ждать проверки.

## Файлы-заметки (в git не попадают, исключены через `.git/info/exclude`)

| Файл | Что в нём |
|---|---|
| `HANDOFF.md` | этот файл: правила, статус, следующий шаг |
| `PLAN.md` | подробный план этапов 0–7 с галочками |
| `sklepPrinciple.md` | разбор исходного стиля проекта и найденных ошибок |

---

## Статус

### ✅ Этап 0 — Подготовка (сделан, отправлен на GitHub: https://github.com/khamutouskaya/sklep-demo)

| Файл | Изменение |
|---|---|
| `.git/` | ветка `main`, remote `origin` → https://github.com/khamutouskaya/sklep-demo |
| `.gitignore` | `target/`, `.idea/`, `src/main/resources/db.properties` |
| `.gitattributes` | `mvnw` с LF, `*.cmd` с CRLF, картинки/jar бинарные |
| `pom.xml` | Java 21 (`maven.compiler.release`), JavaFX 23.0.1 для controls и fxml, версии в `<properties>`, главный класс `SklepApplication` |
| `SklepApplication.java` | бывший `HelloApplication`, открывает `lobby/autoryzacja.fxml`, заголовок «Sklep» |
| `module-info.java` | `opens` только для `controller.admin/klient/lobby`, `exports com.example.sklep` |
| `README.md` | описание, роли, стек, запуск, правила именования |
| Удалены | `HelloController`, `hello-view.fxml`, `lobby/stronaGlowna.txt`, `resources/fxml/autorizacja.fxml` |

Проверено: сборка проходит, окно входа открывается без ошибок.

### ▶ СЛЕДУЮЩИЙ ШАГ — Этап 1, база данных

Перед началом: открыть Docker Desktop.

1. `docker-compose.yml` с PostgreSQL.
2. `db/schema.sql` — таблицы `accounts`, `profiles`, `categories`, `items`, `orders`, `order_items`, `cart_items`.
3. `db/data.sql` — тестовые данные (админ, клиенты, категории, товары).
4. `src/main/resources/db.properties` (в git не попадает) + `db.properties.example`.
5. `DBManager` — настоящее подключение по настройкам из `db.properties`.

Готово, когда: `docker compose up -d` создаёт базу с данными, приложение подключается без ошибок.

### ⏳ Дальше (подробно в PLAN.md)

| № | Этап |
|---|---|
| 2 | Модели и репозитории: исправить SQL, `getGeneratedKeys`, `try-with-resources`, `Optional`, enum'ы |
| 3 | Сервисы: `AuthService` + BCrypt, `CartService`, `OrderService` с транзакцией, `UserService` |
| 4 | Инфраструктура UI: `Navigator`, `style.css`, `fx:include` для шапки и меню, картинки |
| 5 | Экраны по сценариям: вход/регистрация → каталог → товар → корзина → заказ; админка |
| 6 | Качество: JUnit + Mockito, логирование, чистка кода |
| 7 | Презентация: README со скриншотами, GitHub Actions, релиз |

## Известные проблемы исходного кода (исправляются на этапах 2–5)

- `DBManager` передаёт в репозитории `null` вместо подключения.
- SQL в `update` всех репозиториев сломан (нет пробелов, нет `SET col = ?`).
- `OrderRepository.convertToList` — бесконечный цикл; `ProfileRepository.convert` пишет account в address.
- `uzytkownicy.fxml`: `#onBtnBlokuj`/`#onBtnUsun` нет в контроллере — экран не загрузится.
- `dodawanieUzytkownika.fxml` подключён к `DodawanieKategoriiController`.
- `kategorie.fxml`: стиль `gray];` с лишней скобкой.
- Картинок (`фото.jpg`, `платье.jpg` и др.) в проекте нет.
- Контроллеры пустые, переходов между экранами нет, пароли в открытом виде.
