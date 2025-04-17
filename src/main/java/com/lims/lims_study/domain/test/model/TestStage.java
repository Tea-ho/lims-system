package com.lims.lims_study.domain.test.model;

public enum TestStage {
    REQUEST {
        @Override
        public TestStage next() {
            return RECEIPT;
        }

        @Override
        public TestStage previous() {
            return null;
        }
    },
    RECEIPT {
        @Override
        public TestStage next() {
            return RECEIPT_APPROVAL;
        }

        @Override
        public TestStage previous() {
            return REQUEST;
        }
    },
    RECEIPT_APPROVAL {
        @Override
        public TestStage next() {
            return RESULT_INPUT;
        }

        @Override
        public TestStage previous() {
            return RECEIPT;
        }
    },
    RESULT_INPUT {
        @Override
        public TestStage next() {
            return COMPLETED;
        }

        @Override
        public TestStage previous() {
            return RECEIPT_APPROVAL;
        }
    },
    COMPLETED {
        @Override
        public TestStage next() {
            return null;
        }

        @Override
        public TestStage previous() {
            return RESULT_INPUT;
        }
    };

    public abstract TestStage next();
    public abstract TestStage previous();
}