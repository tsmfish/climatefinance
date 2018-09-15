import { ICountry } from 'app/shared/model/country.model';

export const enum Platform {
    MSSQL = 'MSSQL',
    POSTGRESQL = 'POSTGRESQL',
    MYSQL = 'MYSQL',
    ORACLE = 'ORACLE',
    NAVISON = 'NAVISON',
    SUNSYSTEMS = 'SUNSYSTEMS',
    CSV = 'CSV',
    XML = 'XML',
    JSON = 'JSON'
}

export const enum Schedule {
    DAILY = 'DAILY',
    BIWEEKLY = 'BIWEEKLY',
    WEEKLY = 'WEEKLY',
    FORTNIGHTLY = 'FORTNIGHTLY',
    BIMONTHLY = 'BIMONTHLY',
    MONTHLY = 'MONTHLY'
}

export interface IIntegration {
    id?: number;
    platform?: Platform;
    driver?: string;
    username?: string;
    password?: string;
    flatFolder?: string;
    flatFile?: string;
    schedule?: Schedule;
    active?: boolean;
    mapping?: any;
    country?: ICountry;
}

export class Integration implements IIntegration {
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
        public country?: ICountry
    ) {
        this.active = false;
    }
}
