import { BaseEntity } from './../../shared';

export const enum Platform {
    'MSSQL',
    'POSTGRESQL',
    'MYSQL',
    'ORACLE',
    'NAVISON',
    'SUNSYSTEMS',
    'CSV',
    'XML',
    'JSON'
}

export const enum Schedule {
    'DAILY',
    'BIWEEKLY',
    'WEEKLY',
    'FORTNIGHTLY',
    'BIMONTHLY',
    'MONTHLY'
}

export class Integration implements BaseEntity {
    constructor(
        public id?: number,
        public platform?: Platform,
        public driver?: string,
        public username?: string,
        public password?: string,
        public flatFolder?: string,
        public flatFile?: string,
        public schedule?: Schedule,
        public active?: boolean,
        public mapping?: any,
        public country?: BaseEntity,
    ) {
        this.active = false;
    }
}
