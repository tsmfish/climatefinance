import { BaseEntity } from './../../shared';

export class Disbursement implements BaseEntity {
    constructor(
        public id?: number,
        public year?: number,
        public amount?: number,
        public project?: BaseEntity,
    ) {
    }
}
