# sklepPrinciple — стиль и принципы проекта Sklep

Документ описывает, **как на данный момент написано приложение**: структура, соглашения в Java-коде,
в FXML-разметке и в визуальном оформлении. Цель — чтобы новый код и новые экраны выглядели
так же, как уже существующие. В конце — список мест, где сам проект нарушает свои же правила.

---

## 1. Технологии и сборка

| Что | Значение |
|---|---|
| Язык | Java, `source/target = 20` (запускается на JDK 23) |
| UI | JavaFX: `javafx-controls 23.0.1`, `javafx-fxml 17.0.2` |
| БД (план) | PostgreSQL, драйвер `org.postgresql:postgresql:42.7.5`, чистый JDBC (без ORM) |
| Тесты | JUnit 5.9.1 подключён, тестов нет |
| Сборка | Maven + wrapper (`mvnw`, `mvnw.cmd`), запуск `mvnw javafx:run` |
| Модули | JPMS, `module com.example.sklep` в `module-info.java` |
| Пакет | `com.example.sklep` (шаблон IntelliJ «JavaFX project» — сохранены `HelloApplication`, `HelloController`, `hello-view.fxml`) |
| Экраны | рисуются в **Scene Builder** (видно по `xmlns="http://javafx.com/javafx/23.0.1"` и дробным размерам вида `47.599981689453124`) |

---

## 2. Структура каталогов

```
src/main/java/com/example/sklep/
├── HelloApplication.java        точка входа (Application.start)
├── controller/
│   ├── HelloController.java     остаток шаблона
│   ├── admin/                   контроллеры экранов администратора
│   ├── klient/                  контроллеры экранов клиента
│   └── lobby/                   контроллеры экранов гостя / входа
├── db/                          DBManager + *Repository (JDBC)
└── model/                       POJO-модели + enum Role

src/main/resources/com/example/sklep/
├── admin/  klient/  lobby/      FXML — зеркально пакетам контроллеров
└── hello-view.fxml
src/main/resources/fxml/autorizacja.fxml   старая копия экрана входа (без контроллера)
```

**Принцип:** слоистая схема `model → db (repository) → controller → fxml`.
Каждый FXML-файл лежит в папке с тем же именем, что и пакет его контроллера:
`resources/.../admin/towary.fxml` ↔ `controller/admin/TowaryController.java`.

---

## 3. Языки и именование

### 3.1 Какой язык где

| Где | Язык |
|---|---|
| Имена экранов, контроллеров, кнопок, полей ввода | **польский без диакритики**: `Towary`, `Koszyk`, `Uzytkownicy`, `Szukaj`, `Zapisz`, `Usun` |
| Тексты в интерфейсе | польский, диакритика непоследовательно (`Hasło`, `Płać`, но `Ilosc`, `Usun`, `Wyloguj sie`, `Zamowenia`) |
| Модели, репозитории, колонки БД | **английский**: `Account`, `Item`, `CartItem`, `OrderRepository`, `paymentType` |
| Комментарии | **русский** (учебные пояснения) |

### 3.2 Файлы и классы

| Сущность | Правило | Пример |
|---|---|---|
| FXML | `camelCase.fxml`, польское слово | `stronaGlowna.fxml`, `dodawanieTowaru.fxml`, `przegladTowaru.fxml` |
| Контроллер | `PascalCase` имени FXML + `Controller` | `dodawanieTowaru.fxml` → `DodawanieTowaruController` |
| Экран формы добавления | префикс `dodawanie` | `dodawanieKategorii`, `dodawanieUzytkownika` |
| Экран просмотра | префикс `przeglad` | `przegladTowaru`, `przegladTowarow` |
| Главная страница роли | `stronaGlowna` (повторяется в каждой папке, классы различаются только пакетом) | `admin/StronaGlownaController`, `klient/StronaGlownaController` |
| Модель | английское существительное в ед. числе | `Item`, `Order`, `Profile` |
| Репозиторий | `<Модель>Repository` | `ItemRepository` |

### 3.3 Элементы в контроллере (`fx:id` и обработчики)

| Тип | Префикс | Примеры |
|---|---|---|
| Поле ввода (`TextField`/`PasswordField`) | `input` | `inputSzukaj`, `inputLogin`, `inputNazwa`, `inputCena` |
| Метка | `label` | `labelError` |
| Обработчик кнопки | `onBtn` + действие | `onBtnZapisz`, `onBtnSzukaj`, `onBtnMenu`, `onBtnWyloguj`, `onBtnDodaj`, `onBtnUsun`, `onBtnEdytuj` |
| Обработчик гиперссылки | `onLnk` + цель | `onLnkRegistration`, `onLnkTowar1`, `onLnkLeft`, `onLnkRight` |
| Повторяющиеся строки | номер в конце | `onLnkZobacz1..6`, `onBtnUsun1..6`, `onBtnClk1..3` |

Словарь действий (использовать именно эти слова):
`Szukaj` (поиск), `Menu`, `Wyloguj` (выход), `Zaloguj` (вход), `Koszyk` (корзина), `Dodaj` (добавить),
`Edytuj` (изменить), `Usun` (удалить), `Blokuj` (заблокировать), `Zobacz` (посмотреть), `Zapisz` (сохранить), `Plac` (оплатить).

### 3.4 Константы в репозиториях

- Имя таблицы: `private static final String tableName = "accounts";` (camelCase, не UPPER_CASE).
- Колонки: `colId`, `colUsername`, `colRole`… — префикс `col` + имя поля.
- Таблицы во множественном числе (`accounts`, `items`, `orders`, `profiles`, `categories`), исключение — `cartItem`.
- Колонки в camelCase: `isActive`, `paymentType`, `profileId`, `timeStamp`.

---

## 4. Java-код: форматирование и идиомы

- Отступ **4 пробела**, табы не используются.
- Скобки в стиле K&R: `{` на той же строке.
- Пустые строки между методами ставятся непоследовательно (иногда методы идут вплотную).
- Для локальных переменных активно используется **`var`**: `var statement = connection.prepareStatement(...)`.
- Исключения не обрабатываются — пробрасываются наружу: `throws SQLException`.
- Неиспользуемые импорты остаются (`SAXException`, `HashMap`, `LinkedList`, `CertificateNotYetValidException`).
- Логи и `System.out` не используются.

### 4.1 Модели (`model/`)

- Классический **POJO**: `private`-поля → пустой конструктор → полный конструктор → геттеры/сеттеры подряд.
- `id` всегда `long`.
- boolean-поле `isActive` → геттер `isActive()`, сеттер `setActive()` (как генерирует IntelliJ).
- Связи хранятся объектами, а не id: `CartItem.profile`, `CartItem.item`, `CartItem.order`, `Profile.account`.
- Обратные связи — списки: `Item.cartItems`, `Order.cartItems`, `Profile.cart`, `Category.items`.
- Статусы/типы — `String` (`Order.state`, `Order.paymentType`); enum есть только для `Role { ADMIN, USER }`.
- Цена товара называется `precision` (`double`).
- Дата заказа — `java.util.Date`.
- Желаемое правило (комментарий в `Category`: «сделать то же самое везде»): пустой конструктор
  инициализирует поля значениями по умолчанию (`id = 0`, `name = ""`, `items = new ArrayList<>()`).
  Пока сделано только в `Category`.

### 4.2 Репозитории (`db/`)

Все репозитории построены по **одному шаблону** — новый репозиторий надо писать так же:

```java
public class XxxRepository {
    private final Connection connection;
    private static final String tableName = "...";
    private static final String colId = "id";
    // colXxx ...

    public XxxRepository(Connection connection) { this.connection = connection; }

    public long create(Xxx obj) throws SQLException   // INSERT, возвращает getMaxId()
    public void update(Xxx obj) throws SQLException   // UPDATE ... WHERE id = ?
    public void delete(long id) throws SQLException   // DELETE ... WHERE id = ?
    public Xxx getById(long id) throws SQLException   // SELECT * ... WHERE id = ?
    public List<Xxx> getAll() throws SQLException     // SELECT *
    // дополнительные выборки: getByRole(Role) и т.п.

    private long getMaxId()                           // SELECT MAX(id)
    private Xxx convert(ResultSet)                    // одна строка → объект
    private Xxx convertToObj(ResultSet)               // beforeFirst(); нет строк → new Xxx()
    private List<Xxx> convertToList(ResultSet)        // beforeFirst(); while(next) add(convert)
}
```

Детали шаблона:
- SQL собирается конкатенацией констант: `"SELECT * FROM " + tableName + " WHERE " + colId + " = ?"`.
- Значения — только через `PreparedStatement` и `?` (`setString`, `setLong`, `setInt`, `setDouble`, `setBoolean`, `setDate`).
- Над каждым запросом — комментарий с «человеческим» SQL: `//DELETE FROM accounts WHERE colId = ?;`.
- SELECT открывается с `ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY` — это нужно для `beforeFirst()`.
- «Не найдено» = пустой объект (`new Account()`), а не `null` и не исключение.
- Enum читается через `Role.valueOf(resultSet.getString(colRole))`, пишется через `toString()`.
- Связанные объекты подгружаются в `convert()` созданием другого репозитория на том же соединении
  (`new ProfileRepository(connection).getById(...)`) — жадная загрузка.
- Новый id — через `getMaxId()`, а не `getGeneratedKeys()`.

### 4.3 `DBManager`

- Синглтон: `private static DBManager instance`, приватный конструктор, ленивый `getInstance()`.
- Хранит по одному экземпляру каждого репозитория.
- Подключения к БД пока нет — в репозитории передаётся `null`.

### 4.4 Контроллеры (`controller/`)

- Генерируются из Scene Builder (View → Show Sample Controller Skeleton) и сохраняют его формат:
  - `@FXML private TextField inputXxx;` — поля сверху, по алфавиту;
  - `@FXML void onBtnXxx(ActionEvent event) { }` — методы package-private, по алфавиту, тело пустое, между методами пустая строка.
- Начальное состояние экрана задаётся в `public void initialize()` (без `@FXML`), пример: `labelError.setVisible(false)`.
- Порядок в обработчике: «сначала установить значение, потом показать» (`setText` → `setVisible(true)`).
- К БД контроллеры пока не обращаются, переходов между экранами нет.

### 4.5 Точка входа

- `HelloApplication.start()` вручную грузит **один** FXML через `HelloApplication.class.getResource("<папка>/<экран>.fxml")`.
- Нужный экран выбирается комментированием/раскомментированием строк — старые варианты остаются в коде.
- Окно: `new Scene(root, 300, 250)` + `setMinWidth/MinHeight` (с учётом заголовка окна, комментарий «потому что + титул»).
- Заголовок окна всегда `"Logowanie"`.

### 4.6 Комментарии

- Язык — русский, тон учебного конспекта: объясняется, *что делает* API (`//next -- переходит к следующей строке`).
- Ставятся в конце строки после кода без пробела: `requires javafx.fxml;//подключает поддержку FXML`.
- Пометки авторства/работы: `//ja`, `//////////////ja`, `//!!!!!!!!`.
- Встречаются вопросы самому себе и пробный код в `/* ... */` (см. `OrderRepository.create`).
- Иногда комментарий скопирован и не исправлен (`// from string to enum` у не-enum поля, `SELECT * FROM orders` в `ProfileRepository`).

---

## 5. FXML: структура разметки

- Корневой элемент **всегда `GridPane`** с `maxHeight/maxWidth/minHeight/minWidth = "-Infinity"`.
- Раскладка — `GridPane` + вложенные `VBox`/`HBox`; `AnchorPane`, `BorderPane`, CSS-файлы и `TableView` не используются.
- Таблицы (товары, категории, пользователи, корзина) собираются **вручную**: строка = `HBox` с рамкой `gray`,
  ячейка = `Label` фиксированной ширины (`prefWidth="80.0"` / `130.0`), в последней ячейке — маленькие кнопки действий.
- Данные в макетах **захардкожены** (имена `Anna`, `Kacper`; товары `Sukienka czarna`, цены `249,00 PLN`, даты `12.05.2024`).
- У всех кнопок `mnemonicParsing="false"` (Scene Builder).
- Внутренние отступы задаются через `<GridPane.margin><Insets .../></GridPane.margin>`, часто пустые `<Insets />`.
- Шрифт задаётся у каждого элемента отдельно вложенным `<font><Font .../></font>`.
- Изображения: `<Image url="@../фото.jpg" />` — относительные пути, имена файлов на русском/цифрами.
- Порядок атрибутов — как сохраняет Scene Builder (по алфавиту, `GridPane.*` в конце).

### 5.1 Размеры окон

| Тип экрана | Размер корня |
|---|---|
| Рабочие страницы (главная, списки, корзина, заказы, карточка товара) | **600 × 400** |
| Формы добавления (`dodawanie*`) | ~**350 × 270–380** |
| Вход (`autoryzacja`) | **300 × 250** |

### 5.2 Шаблон рабочей страницы (600 × 400)

```
┌─────────┬───────────────────────────────┬──────────────┐
│ [Menu]  │ [ поле поиска ........ ][Szukaj] │ Wyloguj sie / │  ← строка 0, высота 60
│         │                               │ Koszyk/Zaloguj│
├─────────┼───────────────────────────────┼──────────────┤
│ Sukienki│                               │              │
│ Topy    │      основной контент         │  (кнопка     │
│ T-Shirty│   (карточки товаров или       │   Dodaj для  │
│ Spodnie │    «таблица» из HBox-строк)   │   админа)    │
│ Jeansy  │                               │              │
│ Spodnice│                               │              │
│ ─────── │                               │              │
│Kategorie│  ← только у администратора    │              │
│Uzytkown.│                               │              │
│ Towary  │                               │              │
└─────────┴───────────────────────────────┴──────────────┘
 колонка 100        200–400                     100
```

- Колонка 0 (ширина 100, по центру): кнопка `Menu` + боковое меню категорий.
- Колонки 1–2: поиск (`TextField` шириной ~341) и кнопка `Szukaj` (60 × 25, выровнена вправо).
- Колонка 3 (ширина 100): `Wyloguj sie` (гиперссылка, оформленная как кнопка) / `Koszyk` / `Zaloguj`.
- Список категорий всегда одинаковый и в одном порядке: **Sukienki, Topy, T-Shirty, Spodnie, Jeansy, Spodnice**.
- У администратора под `Separator` дополнительно: **Kategorie, Uzytkownicy, Towary**.
- Главная страница: две карточки товара рядом (`VBox` 200 × ~308, фон `lightyellow`, фото 190 × 260 + гиперссылка с названием ЗАГЛАВНЫМИ буквами).

### 5.3 Шаблон формы (вход, dodawanie*)

- Одна колонка, все элементы по центру (`GridPane.halignment="CENTER"`).
- Сверху заголовок: `Label`, шрифт **System Bold 23–26** (`Logowanie`, `Dodanie towaru`, `Dodanie kategorii`, `Dodanie uzytkownika`).
- Далее пары «подпись с двоеточием → поле»: `Nazwa towaru:`, `Cena:`, `Ilosc:`, `Login:`, `Hasło:`.
  Подписи — шрифт размера **17**, ширина подписи = ширине поля (300 в формах, 250 на входе).
- Внизу кнопка `Zapisz` (~116 × 35, шрифт 16) или `Zaloguj` (75 × 30).
- Сообщение об ошибке — `Label` с `textFill="RED"`, по умолчанию скрыт.
- Ссылка на регистрацию — `Hyperlink` «Nie masz jeszcze konta?» под кнопкой.

---

## 6. Визуальный стиль

### 6.1 Палитра

| Цвет | Где используется |
|---|---|
| `F8EDEB` — бледно-розовый/пудровый | фон всех рабочих страниц (600 × 400) |
| `lightyellow` (#FFFFE0) | фон форм и входа, фон кнопок, фон карточек товара |
| `gray` / `grey` | рамки кнопок, строк таблиц, ячеек |
| `white` | плашка с ценой в карточке товара |
| `#030303` (почти чёрный) | текст гиперссылки «Wyloguj sie» |
| `RED` | текст ошибки |

Стили задаются **инлайн** в атрибуте `style`, отдельного `.css` нет. Самые частые сочетания:
- кнопка/ссылка-кнопка: `-fx-background-color: lightyellow; -fx-border-color: gray;`
- строка/ячейка таблицы: `-fx-border-color: gray;`
- контейнер карточки/формы: `-fx-background-color: lightyellow;`
- страница: `-fx-background-color: F8EDEB;`

### 6.2 Шрифты

| Шрифт | Где |
|---|---|
| **Bodoni MT Bold 12** | кнопки `Menu`, `Szukaj`, категории в меню — основной «фирменный» шрифт |
| **Bodoni MT Bold Italic 12** | `Wyloguj sie`, часть категорий (чередуется с Bold) |
| **System Bold 23–26** | заголовки форм |
| System 17 | подписи полей форм |
| System 16 | кнопка `Zapisz` |
| System 13 | заголовки колонок «таблиц» (`Kod`, `Nazwa`, `Cena`, `Ilosc`, `Akcja`, `Status`) |
| System 10–11 | маленькие кнопки действий в строках (`Ed`, `Usun`, `Blok`) |

### 6.3 Тексты интерфейса

- Действия в строках таблиц сокращены: `Ed` (Edytuj), `Blok` (Blokuj), `Usun`, `Zobacz`.
- Названия товаров — ЗАГЛАВНЫМИ, в стиле интернет-магазина одежды:
  `KRÓTKA SUKIENKA Z FALBANKAMI KOLEKCJA ZW`, `SPODNIE Z WYSOKIM STANEM`.
- Цены — формат `249,00 PLN` (запятая, валюта после числа); суммы в корзине — `Laczna kwota : 75,1`.
- Характеристики — «ключ : значение» с пробелами вокруг двоеточия: `Rozmiar :  S`, `Kolor : czarny`.
- Даты — `dd.MM.yyyy` (`23.08.2023`).

---

## 7. Предметная модель

```
Account (username, password, role: ADMIN|USER, isActive)
   ▲ 1
   │
Profile (name, surname, address, account) ──< CartItem (profile, item, order, count) >── Item (name, precision=цена, count, description)
                                                     │
                                                     ▼
                                    Order (timestamp, paymentType, state, comment)
Category (name, items)  ── связь с Item в БД ещё не описана
```

- Корзина и заказ — одна сущность `CartItem`: позиция в корзине получает `orderId`, когда оформляется заказ.
- Три роли интерфейса: **lobby** (гость), **klient** (`Role.USER`), **admin** (`Role.ADMIN`).

---

## 8. Где проект сам нарушает свои правила (на данный момент)

**Именование и тексты**
- Опечатки: `inputSzuakaj` (kategorie), `inputNazwe` (надо `inputNazwa`), `Zamowenia`, `onBtnBlokiruj` (в FXML — `onBtnBlokuj`).
- `fx:id="onBtnEdytuj"` у кнопки в `kategorie.fxml` — префикс обработчика использован как id.
- Смешение `autoryzacja` / `autorizacja`, `Registration` (англ.) среди польских имён.
- Диакритика в текстах то есть, то нет.

**Связка FXML ↔ контроллер**
- `uzytkownicy.fxml` вызывает `#onBtnBlokuj`/`#onBtnUsun`, а в контроллере `onBtnBlokiruj1..6`/`onBtnUsun1..6` → экран не загрузится.
- `dodawanieUzytkownika.fxml` подключён к `DodawanieKategoriiController`.
- `koszyk.fxml` не вызывает ни одного обработчика, хотя в контроллере их 10.

**Оформление**
- `-fx-border-color: gray];` — лишняя `]` (4 раза в `kategorie.fxml`), стиль не применяется.
- `gray` и `grey` вперемешку.
- Шрифт у категорий чередуется Bold / Bold Italic / без шрифта (`T-Shirty`) без системы.
- Bodoni MT есть не на всех ПК — JavaFX молча подставит системный шрифт.
- `xmlns` отсутствует в `autoryzacja.fxml` и `koszyk.fxml`.
- Картинок (`фото.jpg`, `платье.jpg`, `03644304716-e1.jpg`…) в проекте нет.

**Java**
- Пробелы потеряны при конкатенации SQL (`tableName + "SET "` → `accountsSET`), `UPDATE` в `Item/CartItem/Profile` синтаксически неверны.
- `OrderRepository.convertToList` вызывает `convertToObj` → бесконечный цикл.
- `ProfileRepository.convert` записывает account в address; `create` пишет `account.toString()` вместо id.
- `convert()` читает `id` через `getInt`, хотя модель хранит `long`.
- Версии `javafx-controls` (23) и `javafx-fxml` (17) не совпадают.
- Пароли хранятся открытым текстом.

---

## 9. Чек-лист для нового кода «в стиле проекта»

1. Новый экран → `camelCase.fxml` в папке роли (`admin/klient/lobby`), корень `GridPane`, размер 600 × 400 (страница) или ~350 × 300 (форма).
2. Шапка страницы: `Menu` | поиск + `Szukaj` | `Wyloguj sie`/`Koszyk`; слева меню категорий в стандартном порядке.
3. Фон `F8EDEB` для страниц, `lightyellow` для форм; кнопки `lightyellow` + рамка `gray`; шрифт Bodoni MT Bold 12.
4. Контроллер `<Экран>Controller` в пакете `controller.<роль>`; поля `inputXxx`/`labelXxx`, обработчики `onBtnXxx`/`onLnkXxx` с польским глаголом из словаря (п. 3.3).
5. Не забыть `opens com.example.sklep.controller.<роль> to javafx.fxml;` в `module-info.java` для нового пакета.
6. Новая таблица БД → модель-POJO (англ., `long id`) + `XxxRepository` строго по шаблону п. 4.2.
7. Комментарии — по-русски, коротко, рядом с кодом.
