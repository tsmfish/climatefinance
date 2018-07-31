import { BaseEntity } from './../../shared';

export class Country implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public code?: string,
        public projects?: BaseEntity[],
        public integrations?: BaseEntity[],
    ) {
    }
}
