package manager.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");


    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(formatterWriter));
        } else jsonWriter.value((String) null);
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
} 