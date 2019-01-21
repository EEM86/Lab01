package ua.edu.sumdu.j2se.Yermolenko.tasks.Model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskIO {
    private static SimpleDateFormat patternDate = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");

    public static void write(TaskList tasks, OutputStream out) {
        try (DataOutputStream dataOut = new DataOutputStream(out)) {
            dataOut.writeInt(tasks.size());
            for (Task t : tasks) {
                    dataOut.writeInt(t.getTitle().length());
                    dataOut.writeUTF(t.getTitle());
                    dataOut.writeBoolean(t.isActive());
                    dataOut.writeInt(t.getRepeatInterval());

                    if (t.isRepeated()) {
                        dataOut.writeLong(t.getStartTime().getTime());
                        dataOut.writeLong(t.getEndTime().getTime());
                    } else dataOut.writeLong(t.getTime().getTime());
            }
        } catch (IOException e) {
            System.out.println("Can't write the data" + e);
        }
    }

    public static void writeBinary(TaskList tasks, File file) {
        try (FileOutputStream fileForWrite = new FileOutputStream(file)) {
            if (!file.exists()) file.createNewFile();
            write(tasks, fileForWrite);
        } catch(IOException e) {
            System.out.println("Can't create a file" + e);
        }
    }

    public static void read(TaskList tasks, InputStream in) {
        Task task = null;
        try (DataInputStream dataInput = new DataInputStream(in)) {
            int tasksSize = dataInput.readInt();
            while (dataInput.available() > 0) {
                int titleSize = dataInput.readInt();
                String title = dataInput.readUTF();
                Boolean isActive = dataInput.readBoolean();
                int interval = dataInput.readInt();
                if (interval != 0) {
                    Date startTime = new Date(dataInput.readLong());
                    Date endTime = new Date(dataInput.readLong());
                    task = new Task(title, startTime, endTime, interval);
                } else {
                    Date time = new Date(dataInput.readLong());
                    task = new Task(title, time);
                }
                task.setActive(isActive);
                tasks.add(task);
            }
        } catch (EOFException e) {
            System.out.println("catching EOF exc " + e);
        } catch (IOException e) {
            System.out.println("Can't read" + e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                System.out.println("Can't close the input stream" + e);
            }
        }
    }

    public static void readBinary(TaskList tasks, File file) {
        try (FileInputStream fileForRead = new FileInputStream(file)) {
            read(tasks, fileForRead);
        } catch (IOException e) {
            System.out.println("Can't find the file for reading" + e);
        }
    }

    public static void write(TaskList tasks, Writer out) {
            int counter = 1;
            try (BufferedWriter writeOut = new BufferedWriter(out)) {
                for (Task t : tasks) {
                writeOut.write("\"" + t.getTitle() + "\"");
                if (t.isRepeated()) {
                    writeOut.write(" from ");
                    writeOut.write(patternDate.format(t.getStartTime()));
                    writeOut.write(" to ");
                    writeOut.write(patternDate.format(t.getEndTime()));
                    writeOut.write(" every ");
                    writeOut.write(secToDays(t.getRepeatInterval()));
                } else {
                    writeOut.write(" at ");
                    writeOut.write(patternDate.format(t.getTime()));
                }
                if (!t.isActive()) writeOut.write(" inactive");
                if (counter != tasks.size()) {
                    writeOut.write(";\n");
                    counter++;
                } else {
                    writeOut.write(".\n");
                }
            }
        } catch (IOException e) {
                System.out.println("Can't write the data " + e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                System.out.println("Can't close write stream" + e);
            }
        }
    }

    public static void writeText(TaskList tasks, File file) {
        try (FileWriter fileToWrite = new FileWriter(file)) {
            if (!file.exists()) file.createNewFile();
            write(tasks, fileToWrite);
        } catch (IOException e) {
            System.out.println("Can't find a file " + e);
        }
    }

    public static void read(TaskList tasks, Reader in) throws ParseException {
        Task task = null;
        String reading = null;
        String title = null;
        Date time = null;
        Date startTime = null;
        Date endTime = null;
        int interval = 0;

        try (BufferedReader readStream = new BufferedReader(in)) {
            while ((reading = readStream.readLine()) != null) {
                int firstTitleIndex = reading.indexOf('\"');
                int lastTitleIndex = reading.lastIndexOf('\"');
                title = reading.substring(firstTitleIndex + 1, lastTitleIndex);

                Pattern p = Pattern.compile("\\[.+?\\]");
                Matcher m = p.matcher(reading);

                if (!reading.contains("from")) {
                    if (m.find()) {
                        String tmp = m.group();
                        time = patternDate.parse(tmp);
                        task = new Task(title, time);
                    }
                }
                else {
                    String[] tmp = new String[3];
                    int index = 0;
                    while (m.find()) {
                        tmp[index] = m.group();
                        index++;
                    }
                    startTime = patternDate.parse(tmp[0]);
                    endTime = patternDate.parse(tmp[1]);
                    interval = stringToSec(tmp[2]);

                    task = new Task(title, startTime, endTime, interval);
                }
                if (!reading.contains("inactive")) task.setActive(true);
                tasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("Can't read in buffered reader " + e);
        }
    }

    public static void readText(TaskList tasks, File file) throws ParseException {
        try (FileReader fileReader = new FileReader(file)) {
            read(tasks, fileReader);
        } catch (FileNotFoundException e) {
            System.out.println("Can't find the file " + e);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("The file is empty");
        } catch (IOException e) {
            System.out.println("Error in TaskIO readText()" + e);
        }
    }

    private static String secToDays(int seconds) {
        int secs = seconds / 1 % 60;
        int minutes = (seconds / 60 % 60);
        int hours = (seconds / 3600 % 60);
        int days = (seconds / (3600 * 24));
        String result = null;
        String secsStr = (secs == 1 ? "second" : "seconds");
        String minutesStr = (minutes == 1 ? "minute" : "minutes");
        String hoursStr = (hours == 1 ? "hour" : "hours");
        String daysStr = (days == 1 ? "day" : "days");

        if (days == 0 && hours == 0 && minutes == 0) {
            result = String.format("%02d %s" ,secs ,secsStr);
        }
        else if (days == 0 && hours == 0) {
            result = String.format("%02d %s %02d %s", minutes, minutesStr, secs, secsStr);
        }
        else if (days == 0 && hours != 0) {
            result = (String.format("%02d %s %02d %s %02d %s", hours, hoursStr, minutes, minutesStr, secs, secsStr));
        }
        else result = String.format("%02d %s", days, daysStr);

        return ("[" + result + "]");
    }

    private static int stringToSec(String string) {
        String tmp = string.substring(1, string.length() - 1);
        int secs = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;

        String[] arrS = tmp.split(" ");
        for (int j = 0; j < arrS.length; j++) {
            if (arrS[j].equals("days") || arrS[j].equals("day")) {
                days = Integer.parseInt(arrS[j - 1]) * 60 * 60 * 24;
                return days;
            } else {
                if (arrS[j].equals("second") || arrS[j].equals("seconds")) {
                    secs = Integer.parseInt(arrS[j - 1]);
                }
                if (arrS[j].equals("minutes") || arrS[j].equals("minute")) {
                    minutes = Integer.parseInt(arrS[j - 1]) * 60;
                }
                if (arrS[j].equals("hours") || arrS[j].equals("hour")) {
                    hours = Integer.parseInt(arrS[j - 1]) * 60 * 60;
                }
            }
        }
        return hours + minutes + secs;
    }
}
