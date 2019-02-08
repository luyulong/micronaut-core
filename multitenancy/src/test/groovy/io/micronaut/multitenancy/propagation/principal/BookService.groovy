/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.multitenancy.propagation.principal

import io.micronaut.context.annotation.Requires
import io.micronaut.multitenancy.tenantresolver.TenantResolver
import io.micronaut.security.utils.SecurityService

import javax.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Requires(property = 'spec.name', value = 'multitenancy.principal.gorm')
@Singleton
class BookService {
    private final TenantResolver tenantResolver

    BookService(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver
    }

    private final Map<String, List<Book>> books = new ConcurrentHashMap<>()

    Book save(String username, String title) {
        if (!books.containsKey(username)) {
            books.put(username, new ArrayList<>())
        }
        Book b = new Book(title: title)
        books.get(username).add(b)
        return b
    }

    List<Book> list() {
        return books.get(tenantResolver.resolveTenantIdentifier() as String)
    }
}



