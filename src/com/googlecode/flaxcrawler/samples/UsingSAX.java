package com.googlecode.flaxcrawler.samples;

/**
 * boilerpipe
 *
 * Copyright (c) 2009 Christian Kohlschütter
 *
 * The author licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.InputStream;
import java.net.URL;

import org.xml.sax.InputSource;

import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;

/**
 * Demonstrates how to use Boilerpoipe.
 * 
 * @author Christian Kohlschütter
 */
public class UsingSAX {
    public static void main(final String[] args) throws Exception {
        URL url;
        url = new URL(
                "http://proxy3.zju88.net/agent/thread.do?id=Basketball-530ee48f-0bd599783536d9a80064688da24a006e&page=0&bd=Basketball&bp=0&m=0");

        // NOTE We ignore HTTP-based character encoding in this demo...
        final InputStream urlStream = url.openStream();
        final InputSource is = new InputSource(urlStream);

        final BoilerpipeSAXInput in = new BoilerpipeSAXInput(is);
        final TextDocument doc = in.getTextDocument();
        urlStream.close();

        // You have the choice between different Extractors

        // System.out.println(DefaultExtractor.INSTANCE.getText(doc));
        System.out.println(ArticleExtractor.INSTANCE.getText(doc));
    }
}
