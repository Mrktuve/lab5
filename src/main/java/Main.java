import collection.*;
import commands.CommandManager;

/**
 * Точка входа в приложение для управления коллекцией работников.
 * <p>
 * Класс отвечает за инициализацию основных компонентов программы:
 * <ul>
 *   <li>Загрузка имени файла из переменной окружения</li>
 *   <li>Создание менеджеров для работы с данными и файлами</li>
 *   <li>Запуск цикла обработки команд</li>
 * </ul>
 * </p>
 *
 * @author Student
 * @version 1.0
 * @see collection.WorkerCollection
 * @see collection.XmlManager
 * @see commands.CommandManager
 */
public class Main {
    /**
     * Главный метод программы.
     * <p>
     * Выполняет следующие действия:
     * <ol>
     *   <li>Получает имя файла данных из переменной окружения "Worker"</li>
     *   <li>Если переменная не задана, использует файл по умолчанию "lab5.xml"</li>
     *   <li>Создаёт экземпляр XmlManager для работы с XML</li>
     *   <li>Создаёт экземпляр WorkerCollection для хранения данных</li>
     *   <li>Загружает данные из файла в коллекцию</li>
     *   <li>Создаёт и запускает CommandManager для обработки команд</li>
     * </ol>
     * </p>
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        String fileName = System.getenv("Worker");
        if (fileName == null) fileName = "lab5.xml";

        XmlManager xml = new XmlManager();
        WorkerCollection collection = new WorkerCollection();
        collection.getQueue().addAll(xml.load(fileName));


        System.out.println("Запущена коллекция WORKER");
        System.out.println("help - список команд");

        CommandManager manager = new CommandManager(collection, xml, fileName);
        manager.run();
    }
}

