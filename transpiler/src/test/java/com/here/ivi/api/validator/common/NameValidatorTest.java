/*
 * Copyright (C) 2017 HERE Global B.V. and its affiliate(s). All rights reserved.
 *
 * This software, including documentation, is protected by copyright controlled by
 * HERE Global B.V. All rights are reserved. Copying, including reproducing, storing,
 * adapting or translating, any or all of this material requires the prior written
 * consent of HERE Global B.V. This material also contains confidential information,
 * which may not be disclosed to others without prior written consent of HERE Global B.V.
 *
 */

package com.here.ivi.api.validator.common;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.here.ivi.api.model.franca.FrancaElement;
import com.here.ivi.api.model.franca.FrancaModel;
import com.here.ivi.api.test.ArrayEList;
import java.util.LinkedList;
import java.util.List;
import org.franca.core.franca.FModel;
import org.franca.core.franca.FType;
import org.franca.core.franca.FTypeCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class NameValidatorTest {

  private static final String MODEL_NAME = "my.package.name";
  private static final String TYPE_NAME = "myFancyType";
  private static final String INTERFACE_NAME = "Face";

  @Mock private FModel fModel;

  private FrancaModel francaModel;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private FrancaElement francaInterface;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private FrancaElement francaTypeCollection;

  private final List<FrancaElement> typeCollections = new LinkedList<>();
  private final List<FrancaElement> interfaces = new LinkedList<>();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(fModel.getName()).thenReturn(MODEL_NAME);
    when(francaInterface.getName()).thenReturn(INTERFACE_NAME);

    when(francaInterface.getFrancaModel()).thenReturn(fModel);
    when(francaTypeCollection.getFrancaModel()).thenReturn(fModel);

    francaModel = new FrancaModel(null, interfaces, typeCollections);
  }

  @Test
  public void checkTypeNamesWithSingleTypeCollection() {
    typeCollections.add(mockTypeCollectionContainingType(TYPE_NAME, fModel));

    assertTrue(NameValidator.checkTypeNamesInTypeCollection(francaModel));
  }

  @Test
  public void checkTypeNamesWithMultipleTypeCollectionsUniqueTypeNames() {
    typeCollections.add(mockTypeCollectionContainingType(TYPE_NAME, fModel));
    typeCollections.add(mockTypeCollectionContainingType(TYPE_NAME + "2", fModel));

    assertTrue(NameValidator.checkTypeNamesInTypeCollection(francaModel));
  }

  @Test
  public void checkTypeNamesWithMultipleTypeCollectionsNonUniqueTypeNames() {
    typeCollections.add(mockTypeCollectionContainingType(TYPE_NAME, fModel));
    typeCollections.add(mockTypeCollectionContainingType(TYPE_NAME, fModel));

    assertFalse(NameValidator.checkTypeNamesInTypeCollection(francaModel));
  }

  @Test
  public void checkTypeNamesWithMultipleTypeCollectionsNonUniqueTypeNamesDifferentPackages() {
    FModel fModel2 = mock(FModel.class);
    when(fModel2.getName()).thenReturn(MODEL_NAME + ".xtra");

    typeCollections.add(mockTypeCollectionContainingType(TYPE_NAME, fModel));
    typeCollections.add(mockTypeCollectionContainingType(TYPE_NAME, fModel2));

    assertTrue(NameValidator.checkTypeNamesInTypeCollection(francaModel));
  }

  @Test
  public void checkTypeCollectionNamesWithSingleInterface() {
    interfaces.add(francaInterface);

    assertTrue(NameValidator.checkTypeCollectionNames(francaModel));
  }

  @Test
  public void checkTypeCollectionNamesWithSingleTypeCollection() {
    typeCollections.add(francaTypeCollection);

    assertTrue(NameValidator.checkTypeCollectionNames(francaModel));
  }

  @Test
  public void checkTypeCollectionNamesWithTwoUniqueNames() {
    when(francaTypeCollection.getName()).thenReturn(INTERFACE_NAME + "Off");
    interfaces.add(francaInterface);
    typeCollections.add(francaTypeCollection);

    assertTrue(NameValidator.checkTypeCollectionNames(francaModel));
  }

  @Test
  public void checkTypeCollectionNamesWithTwoNonUniqueNames() {
    when(francaTypeCollection.getName()).thenReturn(INTERFACE_NAME);
    interfaces.add(francaInterface);
    typeCollections.add(francaTypeCollection);

    assertFalse(NameValidator.checkTypeCollectionNames(francaModel));
  }

  @Test
  public void checkTypeCollectionNamesWithTwoNonUniqueNamesDifferentPackages() {
    FModel anotherFModel = mock(FModel.class);
    when(anotherFModel.getName()).thenReturn(MODEL_NAME + ".xtra");
    when(francaTypeCollection.getFrancaModel()).thenReturn(anotherFModel);

    when(francaTypeCollection.getName()).thenReturn(INTERFACE_NAME);
    interfaces.add(francaInterface);
    typeCollections.add(francaTypeCollection);

    assertTrue(NameValidator.checkTypeCollectionNames(francaModel));
  }

  private FrancaElement mockTypeCollectionContainingType(String typeName, FModel fModelParam) {

    FType type = mock(FType.class);
    when(type.getName()).thenReturn(typeName);

    FrancaElement typeCollection = mock(FrancaElement.class, Answers.RETURNS_DEEP_STUBS);

    FTypeCollection fTypeCollection = mock(FTypeCollection.class);

    when(typeCollection.getFrancaTypeCollection()).thenReturn(fTypeCollection);
    when(typeCollection.getFrancaModel()).thenReturn(fModelParam);
    when(fTypeCollection.eContainer()).thenReturn(fModelParam);

    ArrayEList<FType> types = new ArrayEList<>();
    types.add(type);
    when(typeCollection.getFrancaTypeCollection().getTypes()).thenReturn(types);
    return typeCollection;
  }
}
