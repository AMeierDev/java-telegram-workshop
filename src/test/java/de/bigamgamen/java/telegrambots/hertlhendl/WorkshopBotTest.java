/*
 * ChatBot Workshop
 * Copyright (C) 2018 Marcus Fihlon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.bigamgamen.java.telegrambots.hertlhendl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.db.MapDBContext;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.*;

public class WorkshopBotTest {

    private static final int USER_ID = 1337;
    private static final long CHAT_ID = 1337L;

    private WorkshopBot bot;
    private DBContext db;
    private MessageSender sender;

    @Before
    public void setUp() {
        // Offline instance will get deleted at JVM shutdown
        db = MapDBContext.offlineInstance("test");
        bot = new WorkshopBot(db);
        sender = mock(MessageSender.class);
        bot.setSender(sender);
        bot.setSilent(new SilentSender(sender));
    }

    @Test
    public void sayHelloWorld() throws TelegramApiException {
        final Update mockedUpdate = mock(Update.class);
        final User user = new User(USER_ID, "Foo", false, "Bar", "foobar42", "en");
        final MessageContext context = MessageContext.newContext(mockedUpdate, user, CHAT_ID);

        bot.sayHelloWorld().action().accept(context);

        final SendMessage message = new SendMessage();
        message.setChatId(CHAT_ID);
        message.setText("Hello world");
        verify(sender, times(1)).execute(message);
    }

    @After
    public void tearDown() {
        db.clear();
    }

}
