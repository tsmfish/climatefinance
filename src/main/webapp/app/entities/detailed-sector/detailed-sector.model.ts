import { BaseEntity } from './../../shared';

export class DetailedSector implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public projects?: BaseEntity[],
    ) {
    }
}
