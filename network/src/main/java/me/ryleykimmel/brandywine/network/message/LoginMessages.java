package me.ryleykimmel.brandywine.network.message;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

/**
 * A Qualifier representing a login message implementation of {@link MessageRegistrar}.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginMessages {

}