import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {SERVER_API_URL} from '../app.constants';
import 'rxjs/add/operator/toPromise';
import {CountryCount} from './countrycount';
import {GenericCount} from './genericcount';
import {Validcountry} from './validcountry';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';

import {catchError, map, tap} from 'rxjs/operators';


@Injectable()
export class HomeService {

    private restUrl = SERVER_API_URL + 'api/custom';

    constructor(private http: HttpClient) {
    }

    getCount(): Observable<String> {
        return this.http.get<String>(this.restUrl + '/count')
    }

    getCountryCount(): Observable<CountryCount[]> {
        return this.http.get<CountryCount[]>(this.restUrl + '/countrycount')
            .pipe(
                catchError(this.handleError('getCountryCount', []))
            );
    }

    getValidCountries(): Observable<Validcountry[]> {
        return this.http.get<Validcountry[]>(this.restUrl + '/validcountries')
            .pipe(
                catchError(this.handleError('getValidCountries', []))
            );
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            // TODO: send the error to remote logging infrastructure
            console.error(error); // log to console instead

            // TODO: better job of transforming error for user consumption
            //this.log(`${operation} failed: ${error.message}`);

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }


    getSectorCount(): Observable<GenericCount[]> {
        return this.http.get<GenericCount[]>(this.restUrl + '/sectorcount')
            .pipe(
                catchError(this.handleError('getSectorCount', []))
            );
    }

    getSectorCountByCountry(countryId): Observable<GenericCount[]> {
        return this.http.get<GenericCount[]>(this.restUrl + '/sectorcountbycountry?countryId=' + countryId)
            .pipe(
                catchError(this.handleError('getGenericCount', []))
            );
    }


    getSourceCount(): Observable<GenericCount[]> {
        return this.http.get<GenericCount[]>(this.restUrl + '/sourcecount')
            .pipe(
                catchError(this.handleError('getSourceCount', []))
            );
    }


}
