package com.appboy.ui.contentcards.handlers;

import com.appboy.events.*;
import java.util.*;
import com.appboy.models.cards.*;

public interface IContentCardsUpdateHandler
{
    List<Card> handleCardUpdate(final ContentCardsUpdatedEvent p0);
}
