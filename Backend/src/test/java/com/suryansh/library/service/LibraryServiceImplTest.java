package com.suryansh.library.service;

import com.suryansh.library.entity.LibraryItemsEntity;
import com.suryansh.library.model.LibraryItemModel;
import com.suryansh.library.repository.LibraryItemsEntityRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryServiceImplTest {
    @Mock
    private LibraryItemsEntityRepo itemsEntityRepo;
    @InjectMocks
    private LibraryServiceImpl libraryService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveNewItem_ItemPresent() {
        // Arrange
        String uniqueId = "test_id";
        LibraryItemsEntity item = LibraryItemsEntity.builder()
                .id(0L)
                .title("Dummy Item")
                .publisher("Dummy Pub")
                .subject("Testing")
                .itemType("OTHER")
                .itemLanguage("Java")
                .uniqueId(uniqueId)
                .build();
        LibraryItemModel itemModel = LibraryItemModel.builder()
                .title("Dummy Item")
                .publisher("Dummy Pub")
                .subject("Testing")
                .itemType(LibraryItemModel.ItemType.OTHER)
                .itemLanguage("Java")
                .build();
        //Mock
        when(itemsEntityRepo.findByTitle(any())).thenReturn(Optional.of(item));
        //Act
        String res = libraryService.saveNewItem(itemModel);
        //Assert
        assertThat(res).isEqualTo("Item saved successfully with unique id " + uniqueId);
    }

    @Test
    void borrowNewItem() {
    }

    @Test
    void returnItemOnTime() {
    }

    @Test
    void returnBookWithFine() {
    }

    public boolean compareItems(LibraryItemsEntity entity, LibraryItemModel model) {
        // Normalize strings by trimming and converting to lowercase
        String normalizedTitle = entity.getTitle().toLowerCase();
        String normalizedPublisher = entity.getPublisher().toLowerCase();
        String normalizedSubject = entity.getSubject().toLowerCase();
        String normalizedItemType = entity.getItemType();
        String normalizedLang = entity.getItemLanguage().toLowerCase();
        return normalizedTitle.equals(model.getTitle().toLowerCase()) &&
                normalizedPublisher.equals(model.getPublisher().toLowerCase()) &&
                normalizedSubject.equals(model.getSubject().toLowerCase()) &&
                normalizedItemType.equals(model.getItemType().name()) &&
                normalizedLang.equals(model.getItemLanguage().toLowerCase());
    }
}