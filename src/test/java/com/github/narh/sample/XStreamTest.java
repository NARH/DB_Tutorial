/*
 * Copyright (c) 2018, NARH https://github.com/NARH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the copyright holder nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.narh.sample;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import lombok.extern.log4j.Log4j;

/**
 * @author narita
 *
 */
@Log4j
public class XStreamTest {

  @Test
  public void serializeTest() throws Exception {
    SampleModel model = new SampleModel();
    model.setVersion("1.0.0");
    model.setChildren(new ArrayList<SampleModel.ChildModel>());

    SampleModel.ChildModel child1 = model.new ChildModel();
    child1.setName("child1");
    child1.setValue("VALUE1");
    model.getChildren().add(child1);

    SampleModel.ChildModel child2 = model.new ChildModel();
    child2.setName("child2");
    child2.setValue("VALUE2");
    model.getChildren().add(child2);

    XStream xstream = new XStream();
    xstream.processAnnotations(SampleModel.class);
    String xml = xstream.toXML(model);
    if(log.isDebugEnabled()) log.debug(xml);
    SampleModel model2 = (SampleModel) xstream.fromXML(xml);
    assertThat("versio", model2.getVersion(), is("1.0.0"));
    assertThat("child size", model2.getChildren().size(), is(2));
    assertThat("child1 name", model2.getChildren().get(0).getName(), is("child1"));
    assertThat("child1 value", model2.getChildren().get(0).getValue(), is("VALUE1"));
    assertThat("child2 name", model2.getChildren().get(1).getName(), is("child2"));
    assertThat("child2 value", model2.getChildren().get(1).getValue(), is("VALUE2"));
    if(log.isDebugEnabled()) log.debug(model2.toString());
  }
}
