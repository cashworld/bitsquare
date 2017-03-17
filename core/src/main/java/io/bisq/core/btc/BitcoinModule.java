/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bisq.core.btc;

import com.google.inject.Singleton;
import io.bisq.common.app.AppModule;
import io.bisq.core.app.AppOptionKeys;
import io.bisq.core.btc.provider.squ.BsqUtxoFeedService;
import io.bisq.core.btc.wallet.BsqWalletService;
import io.bisq.core.btc.wallet.BtcWalletService;
import io.bisq.core.btc.wallet.TradeWalletService;
import io.bisq.core.btc.wallet.WalletsSetup;
import io.bisq.core.provider.fee.FeeService;
import io.bisq.core.provider.price.PriceFeedService;
import io.bisq.network.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.io.File;

import static com.google.inject.name.Names.named;

public class BitcoinModule extends AppModule {
    private static final Logger log = LoggerFactory.getLogger(BitcoinModule.class);

    public BitcoinModule(Environment env) {
        super(env);
    }

    @Override
    protected void configure() {
        bind(RegTestHost.class).toInstance(env.getProperty(BtcOptionKeys.REG_TEST_HOST, RegTestHost.class, RegTestHost.DEFAULT));

        bindConstant().annotatedWith(named(UserAgent.NAME_KEY)).to(env.getRequiredProperty(UserAgent.NAME_KEY));
        bindConstant().annotatedWith(named(UserAgent.VERSION_KEY)).to(env.getRequiredProperty(UserAgent.VERSION_KEY));
        bind(UserAgent.class).in(Singleton.class);

        File walletDir = new File(env.getRequiredProperty(BtcOptionKeys.WALLET_DIR));
        bind(File.class).annotatedWith(named(BtcOptionKeys.WALLET_DIR)).toInstance(walletDir);

        bindConstant().annotatedWith(named(BtcOptionKeys.BTC_NODES)).to(env.getRequiredProperty(BtcOptionKeys.BTC_NODES));
        bindConstant().annotatedWith(named(BtcOptionKeys.USE_TOR_FOR_BTC)).to(env.getRequiredProperty(BtcOptionKeys.USE_TOR_FOR_BTC));
        bindConstant().annotatedWith(named(AppOptionKeys.PROVIDERS)).to(env.getRequiredProperty(AppOptionKeys.PROVIDERS));

        bind(AddressEntryList.class).in(Singleton.class);
        bind(WalletsSetup.class).in(Singleton.class);
        bind(BtcWalletService.class).in(Singleton.class);
        bind(BsqWalletService.class).in(Singleton.class);
        bind(TradeWalletService.class).in(Singleton.class);

        bind(HttpClient.class).in(Singleton.class);
        bind(BsqUtxoFeedService.class).in(Singleton.class);
        bind(PriceFeedService.class).in(Singleton.class);
        bind(FeeService.class).in(Singleton.class);
    }
}
