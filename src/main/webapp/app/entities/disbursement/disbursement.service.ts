import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Disbursement } from './disbursement.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Disbursement>;

@Injectable()
export class DisbursementService {

    private resourceUrl =  SERVER_API_URL + 'api/disbursements';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/disbursements';

    constructor(private http: HttpClient) { }

    create(disbursement: Disbursement): Observable<EntityResponseType> {
        const copy = this.convert(disbursement);
        return this.http.post<Disbursement>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(disbursement: Disbursement): Observable<EntityResponseType> {
        const copy = this.convert(disbursement);
        return this.http.put<Disbursement>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Disbursement>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Disbursement[]>> {
        const options = createRequestOption(req);
        return this.http.get<Disbursement[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Disbursement[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Disbursement[]>> {
        const options = createRequestOption(req);
        return this.http.get<Disbursement[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Disbursement[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Disbursement = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Disbursement[]>): HttpResponse<Disbursement[]> {
        const jsonResponse: Disbursement[] = res.body;
        const body: Disbursement[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Disbursement.
     */
    private convertItemFromServer(disbursement: Disbursement): Disbursement {
        const copy: Disbursement = Object.assign({}, disbursement);
        return copy;
    }

    /**
     * Convert a Disbursement to a JSON which can be sent to the server.
     */
    private convert(disbursement: Disbursement): Disbursement {
        const copy: Disbursement = Object.assign({}, disbursement);
        return copy;
    }
}
