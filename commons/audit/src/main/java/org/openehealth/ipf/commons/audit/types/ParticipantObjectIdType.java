/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit.types;

/**
 * The Participant Object ID Type describes the identifier that is contained in Participant Object ID.
 * Codes from coded terminologies and implementation defined codes can also be used.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public interface ParticipantObjectIdType extends CodedValueType {

    static ParticipantObjectIdType of(String code, String codeSystemName, String originalText) {
        return new ParticipantObjectIdTypeImpl(code, codeSystemName, originalText);
    }

    static ParticipantObjectIdType of(CodedValueType codedValueType) {
        return new ParticipantObjectIdTypeImpl(codedValueType);
    }

    class ParticipantObjectIdTypeImpl extends CodedValueTypeImpl implements ParticipantObjectIdType {
        public ParticipantObjectIdTypeImpl(String code, String codeSystemName, String originalText) {
            super(code, codeSystemName, originalText);
        }

        public ParticipantObjectIdTypeImpl(CodedValueType codedValueType) {
            super(codedValueType);
        }
    }
}
